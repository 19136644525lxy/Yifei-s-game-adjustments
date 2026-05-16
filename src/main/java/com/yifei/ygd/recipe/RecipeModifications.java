package com.yifei.ygd.recipe;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecipeModifications implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeModifications.class);
    
    @Override
    public void onInitialize() {
        // 注册服务器启动事件，在服务器启动时检查配方
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            RecipeManager recipeManager = server.getRecipeManager();
            checkBedrockRecipe(recipeManager);
        });
    }
    
    /**
     * 检查基岩配方是否存在
     * @param recipeManager 配方管理器
     */
    private void checkBedrockRecipe(RecipeManager recipeManager) {
        try {
            // 检查自定义基岩配方是否存在
            Identifier recipeId = new Identifier("ygd", "custom_bedrock");
            if (recipeManager.get(recipeId).isPresent()) {
                LOGGER.info("Custom bedrock recipe found: {}", recipeId);
            } else {
                LOGGER.info("Custom bedrock recipe not found: {}", recipeId);
                LOGGER.info("Make sure the JSON recipe file is in the correct location.");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to check bedrock recipe: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}