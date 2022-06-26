/*     */ package net.minecraft.launcher.updater;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.mojang.launcher.game.process.GameProcessBuilder;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.launcher.CompatibilityRule;
/*     */ import org.apache.commons.lang3.text.StrSubstitutor;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Argument
/*     */ {
/*     */   private final String[] values;
/*     */   private final List<CompatibilityRule> compatibilityRules;
/*     */   
/*     */   public Argument(String[] values, List<CompatibilityRule> compatibilityRules) {
/*  24 */     this.values = values;
/*  25 */     this.compatibilityRules = compatibilityRules;
/*     */   }
/*     */   
/*     */   public void apply(GameProcessBuilder output, CompatibilityRule.FeatureMatcher featureMatcher, StrSubstitutor substitutor) {
/*  29 */     if (appliesToCurrentEnvironment(featureMatcher)) {
/*  30 */       for (String value : this.values) {
/*  31 */         output.withArguments(new String[] { substitutor.replace(value) });
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean appliesToCurrentEnvironment(CompatibilityRule.FeatureMatcher featureMatcher) {
/*  37 */     if (this.compatibilityRules == null) {
/*  38 */       return true;
/*     */     }
/*  40 */     CompatibilityRule.Action lastAction = CompatibilityRule.Action.DISALLOW;
/*  41 */     for (CompatibilityRule compatibilityRule : this.compatibilityRules) {
/*  42 */       CompatibilityRule.Action action = compatibilityRule.getAppliedAction(featureMatcher);
/*  43 */       if (action != null) {
/*  44 */         lastAction = action;
/*     */       }
/*     */     } 
/*  47 */     return (lastAction == CompatibilityRule.Action.ALLOW);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Serializer
/*     */     implements JsonDeserializer<Argument>
/*     */   {
/*     */     public Argument deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/*  56 */       if (json.isJsonPrimitive()) {
/*  57 */         return new Argument(new String[] { json.getAsString() }, null);
/*     */       }
/*     */       
/*     */       try {
/*  61 */         if (json.isJsonObject()) {
/*  62 */           String[] values; JsonObject obj = json.getAsJsonObject();
/*  63 */           JsonElement value = obj.get("values");
/*     */ 
/*     */           
/*  66 */           if (value.isJsonPrimitive()) {
/*  67 */             values = new String[] { value.getAsString() };
/*     */           } else {
/*  69 */             JsonArray array = value.getAsJsonArray();
/*  70 */             values = new String[array.size()];
/*  71 */             for (int i = 0; i < array.size(); i++) {
/*  72 */               values[i] = array.get(i).getAsString();
/*     */             }
/*     */           } 
/*  75 */           List<CompatibilityRule> rules = new ArrayList<>();
/*  76 */           if (obj.has("compatibilityRules")) {
/*  77 */             JsonArray array = obj.getAsJsonArray("compatibilityRules");
/*  78 */             for (JsonElement element : array) {
/*  79 */               rules.add((CompatibilityRule)context.deserialize(element, CompatibilityRule.class));
/*     */             }
/*     */           } 
/*     */           
/*  83 */           return new Argument(values, rules);
/*     */         } 
/*  85 */       } catch (Exception e) {
/*  86 */         if (json.isJsonObject()) {
/*  87 */           String[] values; JsonObject obj = json.getAsJsonObject();
/*  88 */           JsonElement value = obj.get("value");
/*     */ 
/*     */           
/*  91 */           if (value.isJsonPrimitive()) {
/*  92 */             values = new String[] { value.getAsString() };
/*     */           } else {
/*  94 */             JsonArray array = value.getAsJsonArray();
/*  95 */             values = new String[array.size()];
/*  96 */             for (int i = 0; i < array.size(); i++) {
/*  97 */               values[i] = array.get(i).getAsString();
/*     */             }
/*     */           } 
/* 100 */           List<CompatibilityRule> rules = new ArrayList<>();
/* 101 */           if (obj.has("rules")) {
/* 102 */             JsonArray array = obj.getAsJsonArray("rules");
/* 103 */             for (JsonElement element : array) {
/* 104 */               rules.add((CompatibilityRule)context.deserialize(element, CompatibilityRule.class));
/*     */             }
/*     */           } 
/*     */           
/* 108 */           return new Argument(values, rules);
/*     */         } 
/*     */       } 
/*     */       
/* 112 */       throw new JsonParseException("Invalid argument, must be object or string");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\Argument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */