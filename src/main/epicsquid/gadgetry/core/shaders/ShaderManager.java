package epicsquid.gadgetry.core.shaders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import epicsquid.gadgetry.core.GadgetryCore;
import net.minecraft.client.renderer.OpenGlHelper;

public class ShaderManager {
  public static int currentProgram = -1;
  public static int customRenderProgram = 0;

  public static void init() {
    customRenderProgram = loadProgram("/assets/" + GadgetryCore.MODID + "/shaders/custom_render.vs",
        "/assets/" + GadgetryCore.MODID + "/shaders/custom_render.fs");
  }

  public static int loadProgram(String vsh, String fsh) {
    int vertexShader = createShader(vsh, OpenGlHelper.GL_VERTEX_SHADER);
    int fragmentShader = createShader(fsh, OpenGlHelper.GL_FRAGMENT_SHADER);
    int program = OpenGlHelper.glCreateProgram();
    OpenGlHelper.glAttachShader(program, vertexShader);
    OpenGlHelper.glAttachShader(program, fragmentShader);
    OpenGlHelper.glLinkProgram(program);
    return program;
  }

  public static void useProgram(int program) {
    OpenGlHelper.glUseProgram(program);
    currentProgram = program;
  }

  public static int createShader(String filename, int shaderType) {
    int shader = OpenGlHelper.glCreateShader(shaderType);

    if (shader == 0)
      return 0;
    try {
      ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    OpenGlHelper.glCompileShader(shader);
    OpenGlHelper.glCompileShader(shader);

    if (GL20.glGetShaderi(shader, OpenGlHelper.GL_COMPILE_STATUS) == GL11.GL_FALSE)
      throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

    return shader;
  }

  public static String getLogInfo(int obj) {
    return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
  }

  public static String readFileAsString(String filename) throws Exception {
    System.out.println("Loading shader [" + filename + "]...");
    StringBuilder source = new StringBuilder();

    InputStream in = ShaderManager.class.getResourceAsStream(filename);

    String s = "";

    if (in != null) {
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
        s = reader.lines().collect(Collectors.joining("\n"));
      }
    }
    return s;
  }
}
