package me.taylorkelly.bigbrother.tests;

import java.io.File;
import java.sql.SQLException;

import me.taylorkelly.bigbrother.BBSettings;
import me.taylorkelly.bigbrother.BBSettings.DBMS;
import me.taylorkelly.bigbrother.datasource.BBDB;
import me.taylorkelly.bigbrother.tablemgrs.ActionTable;

import org.bukkit.util.config.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestBigBrother {
    private File testFolder;
    
    @Before
    public void setup() {
        this.testFolder = new File("tests");
    }
    
    @Test
    public void configGeneration() {
        File dataFolder = new File(testFolder, "configGeneration");
        dataFolder.mkdirs();
        File settingsFile = new File(dataFolder, "BigBrother.yml");
        BBSettings.initialize(null, dataFolder);
        Assert.assertTrue("Configuration didn't generate.", settingsFile.exists());
    }
    
    @Test
    public void configLoading() throws SQLException {
        File dataFolder = new File(testFolder, "configLoading");
        dataFolder.mkdirs();
        File settingsFile = new File(dataFolder, "BigBrother.yml");
        
        Configuration cfg = new Configuration(settingsFile);
        cfg.setProperty("database.type", "MYSQL");
        cfg.save();
        
        BBSettings.initialize(null, dataFolder);
        
        // Get database running (ActionTable init requires it)
        BBDB.reconnect(); // Do NOT catch.
        
        ActionTable.getInstance().init();
        BBSettings.loadPostponed();
        Assert.assertTrue("Configuration didn't get saved.", BBDB.usingDBMS(DBMS.MYSQL));
    }
}
