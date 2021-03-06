package epicsquid.gadgetry.core.lib.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import epicsquid.gadgetry.core.hax.Hax;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class BakedModelColorWrapper implements IBakedModel {
  IBakedModel internal;
  List<BakedQuad> quads = new ArrayList<>();

  public BakedModelColorWrapper(IBakedModel model) {
    this.internal = model;
  }

  @Override
  public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
    if (quads.size() == 0) {
      List<BakedQuad> list = new ArrayList<>();
      for (EnumFacing f : EnumFacing.values()) {
        list.addAll(internal.getQuads(state, f, rand));
      }
      list.addAll(internal.getQuads(state, null, rand));
      for (int i = 0; i < list.size(); i++) {
        try {
          Hax.bakedQuadTint.set(list.get(i), 0);
        } catch (IllegalArgumentException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      quads.addAll(list);
    }
    return quads;
  }

  @Override
  public boolean isAmbientOcclusion() {
    return internal.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d() {
    return internal.isGui3d();
  }

  @Override
  public boolean isBuiltInRenderer() {
    return internal.isBuiltInRenderer();
  }

  @Override
  public TextureAtlasSprite getParticleTexture() {
    return internal.getParticleTexture();
  }

  @Override
  public ItemOverrideList getOverrides() {
    return internal.getOverrides();
  }

  @Override
  public Pair<? extends IBakedModel, javax.vecmath.Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
    return internal.handlePerspective(type);
  }

}
