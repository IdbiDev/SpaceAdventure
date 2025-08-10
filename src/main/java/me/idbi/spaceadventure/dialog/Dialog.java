package me.idbi.spaceadventure.dialog;


import lombok.Getter;
import lombok.Setter;
import me.idbi.spaceadventure.Main;

@Getter
public class Dialog {

    private final DialogBuilder dialogBuilder;
    private String buffer;
    @Setter private boolean skip;

    public Dialog(DialogBuilder dialogBuilder) {
        this.buffer = "";
        this.dialogBuilder = dialogBuilder;
    }

    public void print() {
        Main.getPlayer().setDialog(this);
        this.buffer = "";
        for (DialogBuilder.Part part : dialogBuilder.getParts()) {
            if(part instanceof DialogBuilder.DialogText dialogText) {
                for (char c : dialogText.getText().toCharArray()) {
                    this.buffer += c;

                    System.out.print("\r" + this.buffer);
                    try {
                        if(!this.skip)
                            Thread.sleep(dialogText.getSpeed());

                    } catch (InterruptedException ignored) {
                    }
                }
                if(skip)
                    skip = false;
            }
            if(part instanceof DialogBuilder.DialogNewLine) {
                System.out.println();
                buffer = "";
            } else if(part instanceof DialogBuilder.DialogCursor dc) {
                if(dc.isCursor()) {
                    Main.getTerminalManager().showCursor();
                } else {
                    Main.getTerminalManager().hideCursor();
                }
            } else if (part instanceof DialogBuilder.DialogTime dt) {
                try {
                    Thread.sleep(dt.getTime());
                } catch (InterruptedException ignored) {}
            }
        }
    }
}