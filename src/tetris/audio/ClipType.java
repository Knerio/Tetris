package tetris.audio;

public enum ClipType {

    BACKGROUND("background.wav"),
    GAME_OVER("game_over.wav"),


    ;

    private final String fileName;

    ClipType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
