package dev.rollczi.litecommands.permssion

import dev.rollczi.litecommands.*
import dev.rollczi.litecommands.valid.ValidationInfo.NO_PERMISSION
import dev.rollczi.litecommands.valid.ValidationInfo.NONE
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PermissionTest : LiteCommandsSpec() {

    private val liteCommand: LiteCommands = LiteTestFactory.builder()
        .command(PermissionCommandTest::class.java)
        .register()

    private val platform = liteCommand.getPlatformManager<Void, LiteTestPlatform>()

    @Test
    fun `test sender with out permission`() {
        val sender = LiteTestSender()
            .ignoreMessages(true)

        assertEquals(NO_PERMISSION, platform.invocation(sender, "main", arrayOf()).validInfo)
        assertEquals(NO_PERMISSION, platform.invocation(sender, "main", arrayOf("inner", "all")).validInfo)
        assertEquals(NONE,          platform.invocation(sender, "main", arrayOf("inner", "without")).validInfo)
        assertEquals(NO_PERMISSION, platform.invocation(sender, "main", arrayOf("inner", "inner")).validInfo)
        assertEquals(NO_PERMISSION, platform.invocation(sender, "main", arrayOf("inner", "main")).validInfo)
    }

    @Test
    fun `test sender with all permission`() {
        val sender = LiteTestSender()
            .ignoreMessages(true)
            .permission("dev.rollczi.main")
            .permission("dev.rollczi.main.inner")

        assertEquals(NONE, platform.invocation(sender, "main", arrayOf()).validInfo)
        assertEquals(NONE, platform.invocation(sender, "main", arrayOf("inner", "all")).validInfo)
        assertEquals(NONE, platform.invocation(sender, "main", arrayOf("inner", "without")).validInfo)
        assertEquals(NONE, platform.invocation(sender, "main", arrayOf("inner", "inner")).validInfo)
        assertEquals(NONE, platform.invocation(sender, "main", arrayOf("inner", "main")).validInfo)
    }

    @Test
    fun `test sender with only main permission`() {
        val sender = LiteTestSender()
            .ignoreMessages(true)
            .permission("dev.rollczi.main")

        assertEquals(NONE,          platform.invocation(sender, "main", arrayOf()).validInfo)
        assertEquals(NO_PERMISSION, platform.invocation(sender, "main", arrayOf("inner", "all")).validInfo)
        assertEquals(NONE,          platform.invocation(sender, "main", arrayOf("inner", "without")).validInfo)
        assertEquals(NO_PERMISSION, platform.invocation(sender, "main", arrayOf("inner", "inner")).validInfo)
        assertEquals(NONE,          platform.invocation(sender, "main", arrayOf("inner", "main")).validInfo)
    }

    @Test
    fun `test sender with only inner permission`() {
        val sender = LiteTestSender()
            .ignoreMessages(true)
            .permission("dev.rollczi.main.inner")

        assertEquals(NO_PERMISSION, platform.invocation(sender, "main", arrayOf()).validInfo)
        assertEquals(NO_PERMISSION, platform.invocation(sender, "main", arrayOf("inner", "all")).validInfo)
        assertEquals(NONE,          platform.invocation(sender, "main", arrayOf("inner", "without")).validInfo)
        assertEquals(NONE,          platform.invocation(sender, "main", arrayOf("inner", "inner")).validInfo)
        assertEquals(NO_PERMISSION, platform.invocation(sender, "main", arrayOf("inner", "main")).validInfo)
    }

}