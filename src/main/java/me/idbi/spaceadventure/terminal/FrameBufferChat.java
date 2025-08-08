package me.idbi.spaceadventure.terminal;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FrameBuffer for terminal rendering with double buffering and diff-based updates.
 * Thread-safe: multiple writers can update, a main thread can call flip() to render changes.
 * (Terminál frame buffer dupla bufferrel és diff alapú frissítéssel, thread-safe működéssel)
 */
public final class FrameBufferChat {

    /** Single cell storing character and colors. (Egy cella karakterrel és színekkel) */
    public static final class Cell {
        char ch;
        TerminalManager.Color fg, bg;
        Cell(char ch, TerminalManager.Color fg, TerminalManager.Color bg) { this.ch = ch; this.fg = fg; this.bg = bg; }
        void set(Cell o){ this.ch=o.ch; this.fg=o.fg; this.bg=o.bg; }
        @Override public boolean equals(Object o){
            if(!(o instanceof Cell c)) return false;
            return ch==c.ch && fg==c.fg && bg==c.bg;
        }
        @Override public int hashCode(){ return Objects.hash(ch,fg,bg); }
    }

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private int width, height;
    private Cell[] front, back;

    private static final String CSI = "\u001B[";
    private static final String RESET = CSI + "0m";
    private TerminalManager.Color curFg = TerminalManager.Color.WHITE, curBg = null;
    private int cx = 0, cy = 0;

    private static final Pattern SGR = Pattern.compile("\u001B\\[([0-9;]*)m");

    /**
     * Creates a new FrameBuffer with given size. (Új FrameBuffer adott mérettel)
     * @param width  Buffer width in characters (szélesség karakterben)
     * @param height Buffer height in characters (magasság karakterben)
     */
    public FrameBufferChat(int width, int height){
        if(width<=0||height<=0) throw new IllegalArgumentException("Invalid size");
        this.width=width; this.height=height;
        this.front = allocFilled(width,height,new Cell(' ', TerminalManager.Color.WHITE, null));
        this.back  = allocFilled(width,height,new Cell(' ', TerminalManager.Color.WHITE, null));
    }

    /**
     * Sets the current print style (Beállítja az aktuális írás stílusát).
     * @param fg Foreground color (null to keep current)
     * @param bg Background color (null for terminal default)
     */
    public void setStyle(TerminalManager.Color fg, TerminalManager.Color bg){
        lock.writeLock().lock();
        try { if(fg!=null) curFg=fg; curBg=bg; } finally { lock.writeLock().unlock(); }
    }

    /**
     * Sets the cursor position (Beállítja a kurzor pozícióját).
     * @param x Column (0-based)
     * @param y Row (0-based)
     */
    public void setCursor(int x, int y){
        lock.writeLock().lock();
        try { cx=clamp(x,0,width-1); cy=clamp(y,0,height-1); } finally { lock.writeLock().unlock(); }
    }

    /**
     * Moves the cursor by dx/dy relative to current position (Mozgatja a kurzort relatívan).
     * @param dx Columns to move (oszlop eltolás)
     * @param dy Rows to move (sor eltolás)
     */
    public void moveCursor(int dx, int dy){
        lock.writeLock().lock();
        try { setCursor(cx+dx, cy+dy); } finally { lock.writeLock().unlock(); }
    }

    /**
     * Clears the back buffer with spaces (Törli a back buffert szóközökkel).
     */
    public void clear(){
        lock.writeLock().lock();
        try {
            for (Cell c : back) { c.ch=' '; c.fg=TerminalManager.Color.WHITE; c.bg=null; }
            cx=0; cy=0;
        } finally { lock.writeLock().unlock(); }
    }

    /**
     * Resizes the buffer and clears its content (Átméretezi a buffert és törli a tartalmát).
     * @param w New width
     * @param h New height
     */
    public void resize(int w, int h){
        if(w<=0||h<=0) return;
        lock.writeLock().lock();
        try {
            this.width=w; this.height=h;
            this.front = allocFilled(w,h,new Cell(' ', TerminalManager.Color.WHITE, null));
            this.back  = allocFilled(w,h,new Cell(' ', TerminalManager.Color.WHITE, null));
            cx=cy=0;
        } finally { lock.writeLock().unlock(); }
    }

    /**
     * Sets a single cell's character and colors (Beállít egy cellát).
     * @param x Column (0-based)
     * @param y Row (0-based)
     * @param ch Character to set
     * @param fg Foreground color
     * @param bg Background color
     */
    public void putChar(int x, int y, char ch, TerminalManager.Color fg, TerminalManager.Color bg){
        lock.writeLock().lock();
        try {
            if(!inBounds(x,y)) return;
            Cell c = back[idx(x,y)];
            c.ch=ch; c.fg=(fg!=null?fg:curFg); c.bg=bg;
        } finally { lock.writeLock().unlock(); }
    }

    /**
     * Prints a single character at cursor and moves cursor forward (Kiír egy karaktert a kurzor helyére).
     * Supports \n, \r, \t.
     * @param ch Character to print
     */
    public void print(char ch){
        lock.writeLock().lock();
        try {
            if(ch=='\n'){ newline(); return; }
            if(ch=='\r'){ cx=0; return; }
            if(ch=='\t'){ int next = ((cx/4)+1)*4; while(cx<next) rawPut(' '); return; }
            rawPut(ch);
        } finally { lock.writeLock().unlock(); }
    }

    /**
     * Prints a string starting at the cursor (Kiír egy szöveget a kurzor helyéről).
     * Supports \n, \r, \t.
     * @param s Text to print
     */
    public void print(String s){
        if(s==null) return;
        lock.writeLock().lock();
        try {
            for (int i=0;i<s.length();i++) {
                char ch=s.charAt(i);
                if(ch=='\n'){ newline(); continue; }
                if(ch=='\r'){ cx=0; continue; }
                if(ch=='\t'){ int next=((cx/4)+1)*4; while(cx<next) rawPut(' '); continue; }
                rawPut(ch);
            }
        } finally { lock.writeLock().unlock(); }
    }

    /**
     * Prints a string with ANSI color codes interpreted (Kiír egy ANSI színkódokat tartalmazó szöveget).
     * @param s ANSI string
     */
    public void printAnsi(String s){
        if(s==null) return;
        lock.writeLock().lock();
        try {
            Matcher m = SGR.matcher(s);
            int i=0;
            while(i<s.length()){
                if(m.find(i) && m.start()==i){
                    applySgr(m.group(1));
                    i = m.end();
                }else{
                    char ch = s.charAt(i++);
                    if(ch=='\n'){ newline(); continue; }
                    if(ch=='\r'){ cx=0; continue; }
                    if(ch=='\t'){ int next=((cx/4)+1)*4; while(cx<next) rawPut(' '); continue; }
                    rawPut(ch);
                }
            }
        } finally { lock.writeLock().unlock(); }
    }

    /**
     * Prints a newline (Új sort kezd).
     */
    public void println(){ print('\n'); }

    /**
     * Prints text followed by a newline (Szöveg kiírása új sorral).
     * @param s Text
     */
    public void println(String s){ print(s); print('\n'); }

    /**
     * Renders changes between back and front buffers to output (Kirajzolja a változásokat).
     * @param out Appendable output (e.g. JLine Terminal writer)
     */
    public void flip(Appendable out) throws IOException {
        final int w,h; final Cell[] fSnap,bSnap;
        lock.readLock().lock();
        try { w=width; h=height; fSnap=copy(front); bSnap=copy(back); }
        finally { lock.readLock().unlock(); }

        int curX=-1, curY=-1; TerminalManager.Color aFg=null, aBg=null;
        StringBuilder sb = new StringBuilder(w*h/2);

        for(int y=0;y<h;y++){
            int x=0;
            while(x<w){
                int i= y*w + x;
                if(bSnap[i].equals(fSnap[i])){ x++; continue; }
                TerminalManager.Color runFg=bSnap[i].fg, runBg=bSnap[i].bg;
                int start=x, end=x+1;
                while(end<w){
                    int j=y*w+end;
                    if(!bSnap[j].equals(fSnap[j]) && runFg==bSnap[j].fg && runBg==bSnap[j].bg) end++;
                    else break;
                }
                if(curY!=y || curX!=start){
                    sb.append(CSI).append(y+1).append(';').append(start+1).append('H');
                    curY=y; curX=start;
                }
                if(aFg!=runFg || aBg!=runBg){
                    sb.append(RESET);
                    if(runFg!=null) sb.append(runFg.toString());
                    if(runBg!=null) sb.append(runBg.toString());
                    aFg=runFg; aBg=runBg;
                }
                for(int xx=start; xx<end; xx++){
                    sb.append(bSnap[y*w+xx].ch);
                }
                for(int xx=start; xx<end; xx++) fSnap[y*w+xx].set(bSnap[y*w+xx]);
                curX=end;
                x=end;
            }
        }
        sb.append(RESET);
        out.append(sb);

        lock.writeLock().lock();
        try {
            if(front.length==fSnap.length){
                for(int i=0;i<front.length;i++) front[i].set(fSnap[i]);
            }else{
                for (Cell c : front){ c.ch=' '; c.fg=TerminalManager.Color.WHITE; c.bg=null; }
            }
        } finally { lock.writeLock().unlock(); }
    }

    /**
     * Switches to alt screen, hides cursor, resets styles (Alt képernyő, kurzor elrejtés, reset).
     * @param out Appendable output
     */
    public void begin(Appendable out) throws IOException {
        out.append(CSI).append("?1049h");
        out.append(CSI).append("?25l");
        out.append(CSI).append("H");
        out.append(RESET);
    }

    /**
     * Restores normal screen, shows cursor, resets styles (Visszaállít normál képernyőre).
     * @param out Appendable output
     */
    public void end(Appendable out) throws IOException {
        out.append(RESET);
        out.append(CSI).append("?25h");
        out.append(CSI).append("?1049l");
    }

    // ===== Helpers =====
    private void rawPut(char ch){
        if(cy<0||cy>=height) return;
        if(cx<0){ cx=0; }
        if(cx>=width){ newline(); }
        if(cy>=height) return;
        Cell c = back[idx(cx,cy)];
        c.ch=ch; c.fg=curFg; c.bg=curBg;
        cx++;
    }
    private void newline(){ cx=0; cy++; if(cy>=height){ cy=height-1; scrollUp(); } }
    private void scrollUp(){
        System.arraycopy(back, width, back, 0, back.length - width);
        for(int i=back.length - width; i<back.length; i++){
            back[i].ch=' '; back[i].fg=TerminalManager.Color.WHITE; back[i].bg=null;
        }
    }
    private void applySgr(String params){
        if(params==null || params.isEmpty()){ curFg=TerminalManager.Color.WHITE; curBg=null; return; }
        String[] ps = params.split(";");
        for(String p : ps){
            if(p.isEmpty()) continue;
            int n;
            try{ n=Integer.parseInt(p); }catch(Exception e){ continue; }
            if(n==0){ curFg=TerminalManager.Color.WHITE; curBg=null; continue; }
            if(n>=30 && n<=37) curFg = mapFg(n);
            if(n>=90 && n<=97) curFg = mapFg(n);
            if(n>=40 && n<=47) curBg = mapBg(n);
            if(n>=100 && n<=107) curBg = mapBg(n);
        }
    }
    private TerminalManager.Color mapFg(int code){
        return switch (code) {
            case 30 -> TerminalManager.Color.BLACK;
            case 31 -> TerminalManager.Color.RED;
            case 32 -> TerminalManager.Color.GREEN;
            case 33 -> TerminalManager.Color.YELLOW;
            case 34 -> TerminalManager.Color.BLUE;
            case 35 -> TerminalManager.Color.MAGENTA;
            case 36 -> TerminalManager.Color.CYAN;
            case 37 -> TerminalManager.Color.WHITE;
            case 90 -> TerminalManager.Color.BRIGHT_BLACK;
            case 91 -> TerminalManager.Color.BRIGHT_RED;
            case 92 -> TerminalManager.Color.BRIGHT_GREEN;
            case 93 -> TerminalManager.Color.BRIGHT_YELLOW;
            case 94 -> TerminalManager.Color.BRIGHT_BLUE;
            case 95 -> TerminalManager.Color.BRIGHT_MAGENTA;
            case 96 -> TerminalManager.Color.BRIGHT_CYAN;
            case 97 -> TerminalManager.Color.BRIGHT_WHITE;
            default -> curFg;
        };
    }
    private TerminalManager.Color mapBg(int code){
        return switch (code) {
            case 40 -> TerminalManager.Color.BLACK_BACKGROUND;
            case 41 -> TerminalManager.Color.RED_BACKGROUND;
            case 42 -> TerminalManager.Color.GREEN_BACKGROUND;
            case 43 -> TerminalManager.Color.YELLOW_BACKGROUND;
            case 44 -> TerminalManager.Color.BLUE_BACKGROUND;
            case 45 -> TerminalManager.Color.MAGENTA_BACKGROUND;
            case 46 -> TerminalManager.Color.CYAN_BACKGROUND;
            case 47 -> TerminalManager.Color.WHITE_BACKGROUND;
            case 100 -> TerminalManager.Color.BRIGHT_BLACK_BACKGROUND;
            case 101 -> TerminalManager.Color.BRIGHT_RED_BACKGROUND;
            case 102 -> TerminalManager.Color.BRIGHT_GREEN_BACKGROUND;
            case 103 -> TerminalManager.Color.BRIGHT_YELLOW_BACKGROUND;
            case 104 -> TerminalManager.Color.BRIGHT_BLUE_BACKGROUND;
            case 105 -> TerminalManager.Color.BRIGHT_MAGENTA_BACKGROUND;
            case 106 -> TerminalManager.Color.BRIGHT_CYAN_BACKGROUND;
            case 107 -> TerminalManager.Color.BRIGHT_WHITE_BACKGROUND;
            default -> curBg;
        };
    }
    private static Cell[] allocFilled(int w,int h, Cell fill){
        Cell[] a = new Cell[w*h];
        for(int i=0;i<a.length;i++) a[i]=new Cell(fill.ch, fill.fg, fill.bg);
        return a;
    }
    private static Cell[] copy(Cell[] s){
        Cell[] d=new Cell[s.length];
        for(int i=0;i<s.length;i++){ Cell c=s[i]; d[i]=new Cell(c.ch,c.fg,c.bg); }
        return d;
    }
    private int idx(int x,int y){ return y*width+x; }
    private boolean inBounds(int x,int y){ return x>=0&&y>=0&&x<width&&y<height; }
    private static int clamp(int v,int lo,int hi){ return Math.max(lo, Math.min(hi, v)); }

    public static void main(String[] args) {
        FrameBufferChat chat = new FrameBufferChat(50, 50);
        chat.printAnsi("KURVA ANYAD");
        try {
            chat.flip(System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
