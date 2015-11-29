package com.team846.frc2015;

import com.team846.frc2015.config.RootConfig;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class MonadicConfigTest {
    private RootConfig rootConfig;

    @Before
    public void loadConfig() {
        rootConfig = new RootConfig(new File(getClass().getResource("/test-conf.conf").getFile()));
    }

    @Test
    public void rootValue() {
        assert rootConfig.get().map(root -> root.getConfig("robot")).map(config -> config.getInt("size")).get() == 10;
    }
}
