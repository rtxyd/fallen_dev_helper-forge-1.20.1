package net.rtxyd.fallen_lib.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.rtxyd.fallen_lib.config.FallenConfig;
import net.rtxyd.fallen_lib.service.FallenPatchEntry;
import net.rtxyd.fallen_lib.type.engine.Resource;
import net.rtxyd.fallen_lib.type.engine.ResourceProcessor;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

class FallenConfigProcessor implements ResourceProcessor {



    @Override
    public boolean supports(Resource r) {
        return r.path().endsWith(".fallen.json");
    }

    @Override
    public void process(Resource r, ScanContext ctx) {
        try (Reader reader = new InputStreamReader(r.open(), StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().create();
            FallenConfig cfg = gson.fromJson(reader, FallenConfig.class);
            if (!cfg.versionCheck()) {
                ResourceScanEngine.LOGGER.warn("Version mismatches: {}", r.path());
                return;
            } else if (!cfg.isRequired()) {
                return;
            }
            ctx.internalConfigContainers.put(cfg, r.container());
        } catch (Exception e) {
            ResourceScanEngine.LOGGER.error("Failed reading fallen config: {}", r.path());
        }
    }

}
