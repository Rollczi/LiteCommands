package dev.rollczi.litecommands.permssion

import dev.rollczi.litecommands.InvocationCreator
import dev.rollczi.litecommands.LiteCommandsSpec
import dev.rollczi.litecommands.LiteInvocation
import dev.rollczi.litecommands.component.LiteComponent
import dev.rollczi.litecommands.component.LiteSection
import dev.rollczi.litecommands.valid.ValidationInfo
import groovy.transform.CompileStatic
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@CompileStatic
class PermissionTest : LiteCommandsSpec() {

    private val command: PermissionCommandTest = PermissionCommandTest()
    private val section: LiteSection = factory.createSection(command).get()

    private val invocationMainPermOnly = InvocationCreator { sender ->
        LiteComponent.ContextOfResolving.create(LiteInvocation("main", sender, arrayOf()))
    }
    private val invocationAllPerm = InvocationCreator { sender ->
        LiteComponent.ContextOfResolving.create(LiteInvocation("main", sender, arrayOf("inner", "all")))
    }
    private val invocationWithoutPerm = InvocationCreator { sender ->
        LiteComponent.ContextOfResolving.create(LiteInvocation("main", sender, arrayOf("inner", "without")))
    }
    private val invocationInnerPerm = InvocationCreator { sender ->
        LiteComponent.ContextOfResolving.create(LiteInvocation("main", sender, arrayOf("inner", "inner")))
    }
    private val invocationMainPerm = InvocationCreator { sender ->
        LiteComponent.ContextOfResolving.create(LiteInvocation("main", sender, arrayOf("inner", "main")))
    }

    @Test
    fun `test sender with out permission`() {
        val sender = TestPermissionSender()

        assertEquals(ValidationInfo.NO_PERMISSION, section.resolveExecution(invocationMainPermOnly.get(sender)).validInfo)
        assertEquals(ValidationInfo.NO_PERMISSION, section.resolveExecution(invocationAllPerm.get(sender)).validInfo)
        assertEquals(ValidationInfo.NONE, section.resolveExecution(invocationWithoutPerm.get(sender)).validInfo)
        assertEquals(ValidationInfo.NO_PERMISSION, section.resolveExecution(invocationInnerPerm.get(sender)).validInfo)
        assertEquals(ValidationInfo.NO_PERMISSION, section.resolveExecution(invocationMainPerm.get(sender)).validInfo)
    }

    @Test
    fun `test sender with all permission`() {
        val sender = TestPermissionSender()
            .permission("dev.rollczi.main")
            .permission("dev.rollczi.main.inner")

        assertEquals(ValidationInfo.NONE, section.resolveExecution(invocationMainPermOnly.get(sender)).validInfo)
        assertEquals(ValidationInfo.NONE, section.resolveExecution(invocationAllPerm.get(sender)).validInfo)
        assertEquals(ValidationInfo.NONE, section.resolveExecution(invocationWithoutPerm.get(sender)).validInfo)
        assertEquals(ValidationInfo.NONE, section.resolveExecution(invocationInnerPerm.get(sender)).validInfo)
        assertEquals(ValidationInfo.NONE, section.resolveExecution(invocationMainPerm.get(sender)).validInfo)
    }

    @Test
    fun `test sender with only main permission`() {
        val sender = TestPermissionSender()
            .permission("dev.rollczi.main")

        assertEquals(ValidationInfo.NONE, section.resolveExecution(invocationMainPermOnly.get(sender)).validInfo)
        assertEquals(ValidationInfo.NO_PERMISSION, section.resolveExecution(invocationAllPerm.get(sender)).validInfo)
        assertEquals(ValidationInfo.NONE, section.resolveExecution(invocationWithoutPerm.get(sender)).validInfo)
        assertEquals(ValidationInfo.NO_PERMISSION, section.resolveExecution(invocationInnerPerm.get(sender)).validInfo)
        assertEquals(ValidationInfo.NONE, section.resolveExecution(invocationMainPerm.get(sender)).validInfo)
    }

    @Test
    fun `test sender with only inner permission`() {
        val sender = TestPermissionSender()
            .permission("dev.rollczi.main.inner")

        assertEquals(ValidationInfo.NO_PERMISSION, section.resolveExecution(invocationMainPermOnly.get(sender)).validInfo)
        assertEquals(ValidationInfo.NO_PERMISSION, section.resolveExecution(invocationAllPerm.get(sender)).validInfo)
        assertEquals(ValidationInfo.NONE, section.resolveExecution(invocationWithoutPerm.get(sender)).validInfo)
        assertEquals(ValidationInfo.NONE, section.resolveExecution(invocationInnerPerm.get(sender)).validInfo)
        assertEquals(ValidationInfo.NO_PERMISSION, section.resolveExecution(invocationMainPerm.get(sender)).validInfo)
    }

}