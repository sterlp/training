package org.sterl.training.chatgpt;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.hexadevlabs.gpt4all.LLModel;

class TestGpt {
    
    static LLModel.GenerationConfig config = LLModel.config()
            .withTemp(0.33f)
            .withNPredict(1024).build();

    @Test
    void test() throws Exception {
        // Replace the hardcoded path with the actual path where your model file resides
        String modelFilePath = "/Users/sterlp/dev/gpt4/ggml-vicuna-7b-1.1-q4_2.bin";

        try (LLModel model = new LLModel(Path.of(modelFilePath))) {
            model.setThreadCount(Runtime.getRuntime().availableProcessors() / 2);
            String old = promt("Top 3 Eigenschaften von Angular gegenüber React.", model, null);
            old = promt("Welches der Framweworks kann man schneller erlernen?", model, old);
        }
    }
    
    static String promt(String human, LLModel model, String old) {
        String prompt = (old != null ? old + "\n" : "") + "### Human:\n" + human + "\n### Assistant:\n";
        prompt += model.generate(prompt, config, true);
        return prompt;
    }

}
