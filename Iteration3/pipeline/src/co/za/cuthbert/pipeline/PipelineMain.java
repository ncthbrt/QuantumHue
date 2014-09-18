package co.za.cuthbert.pipeline;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Copyright Nick Cuthbert, 2014
 */
public class PipelineMain {
    private static final String sourceAssetsDir ="assets/";
    private static final String sourceTexturesDir =sourceAssetsDir+"textures";
    private static final String sourceOtherAssetsDir =sourceAssetsDir+"other";
    private static final String sourceCollidableAssetsDir =sourceAssetsDir+"collidable";
    private static final String androidDir="android";
    private static final String coreDir="android";
    private static final String atlasFileName ="global";

    public static void main (String[] args){

            String destination;
            if (new File("android").exists()) {
                destination = "android/assets/";
            } else {
                destination = "core/assets/";
            }
            try {
                File destinationFile = new File(destination);
                if (!destinationFile.exists()) {

                    destinationFile.mkdir();
                } else {
                    FileUtils.cleanDirectory(destinationFile);
                }
                TexturePacker.process(sourceTexturesDir, destination, atlasFileName);
                FileUtils.copyDirectory(new File(sourceOtherAssetsDir), destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
