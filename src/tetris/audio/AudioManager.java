package tetris.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AudioManager {

    private final Map<ClipType, Clip> clips = new HashMap<>();

    public CompletableFuture<Void> playAudio(ClipType type) {
        return playAudio(type, false);
    }

    public void stopAudio(ClipType type) {
        Clip clip = clips.get(type);
        if (clip != null) {
            clip.stop();
            clip.close();
            clips.remove(type);
        }
    }



    public CompletableFuture<Void> playAudio(ClipType type, boolean loop) {
        if (clips.containsKey(type)) {
            return CompletableFuture.completedFuture(null);
        }

        final CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("./res/audio", type.getFileName()).getAbsoluteFile());
            final Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            clips.put(type, clip);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP && !clip.isRunning()) {
                    future.complete(null);
                    clip.close();
                }
            });
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }

        return future;
    }

}
