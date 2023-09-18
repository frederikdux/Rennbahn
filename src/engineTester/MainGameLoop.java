package engineTester;

import Entities.*;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import renderEngine.EntityRenderer;
import shaders.StaticShader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {
    public static void main(String[] args) {
        Mouse.setGrabbed(true);

        DisplayManager.createDisplay();
        Loader loader = new Loader();


        //********TERRAIN TEXTURE STUFF********

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        //*************************************


        ModelData data = OBJFileLoader.loadOBJ("stall");
        RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        TexturedModel staticModel = new TexturedModel(model, texture);

        ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
        RawModel grassModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
        ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grassTexture"));
        grassTexture.setShineDamper(10);
        grassTexture.setReflectivity(1);
        TexturedModel grassStaticModel = new TexturedModel(grassModel, grassTexture);
        grassStaticModel.getTexture().setHasTransparency(true);
        grassStaticModel.getTexture().setUseFakeLighting(true);

        Entity entity = new Entity(staticModel, new Vector3f(0, 0, -20), 0, 180, 0, 1);
        Entity grass = new Entity(grassStaticModel, new Vector3f(-10, 0, -10), 0, 0, 0, 1);
        Light light = new Light(new Vector3f(10, 100, 10), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
        //Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");

        MasterRenderer renderer = new MasterRenderer();

        ModelData playerModelData = OBJFileLoader.loadOBJ("grassModel");
        RawModel rawPlayerModel = loader.loadToVAO(playerModelData.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        TexturedModel playerModel = new TexturedModel(rawPlayerModel, new ModelTexture(loader.loadTexture("skin")));

        Player player = new Player(playerModel, new Vector3f(100, 0, -50), 0, 0, 0, 1);

        Camera camera = new Camera(player);

        while(!Display.isCloseRequested()){
            player.move(terrain);
            camera.move();
            renderer.processEntity(player);
            renderer.processTerrain(terrain);
            //renderer.processTerrain(terrain2);
            renderer.processEntity(entity);
            renderer.processEntity(grass);

            renderer.render(light, camera);
            DisplayManager.updateDisplay();

            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
                DisplayManager.closeDisplay();
            }
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
