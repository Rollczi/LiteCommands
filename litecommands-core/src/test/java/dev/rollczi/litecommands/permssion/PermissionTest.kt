package dev.rollczi.litecommands.permssion

import dev.rollczi.litecommands.InvocationCreator
import dev.rollczi.litecommands.LiteCommandsSpec
import dev.rollczi.litecommands.LiteInvocation
import dev.rollczi.litecommands.component.LiteComponent
import dev.rollczi.litecommands.component.LiteSection
import dev.rollczi.litecommands.valid.ValidationCommandException
import groovy.transform.CompileStatic
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@CompileStatic
class PermissionTest : LiteCommandsSpec() {

    private val command: PermissionCommandTest = PermissionCommandTest()
    private val section: LiteSection = factory.createSection(command).get()

    private val invocationMainPermOnly = InvocationCreator { sender ->
        LiteComponent.Data.create(LiteInvocation("main", sender, arrayOf()))
    }
    private val invocationAllPerm = InvocationCreator { sender ->
        LiteComponent.Data.create(LiteInvocation("main", sender, arrayOf("inner", "all")))
    }
    private val invocationWithoutPerm = InvocationCreator { sender ->
        LiteComponent.Data.create(LiteInvocation("main", sender, arrayOf("inner", "without")))
    }
    private val invocationInnerPerm = InvocationCreator { sender ->
        LiteComponent.Data.create(LiteInvocation("main", sender, arrayOf("inner", "inner")))
    }
    private val invocationMainPerm = InvocationCreator { sender ->
        LiteComponent.Data.create(LiteInvocation("main", sender, arrayOf("inner", "main")))
    }

    @Test
    fun `test sender with out permission`() {
        val sender = TestPermissionSender()

        assertThrows(ValidationCommandException::class.java) { section.resolve(invocationMainPermOnly.get(sender)) }
        assertThrows(ValidationCommandException::class.java) { section.resolve(invocationAllPerm.get(sender)) }
        assertDoesNotThrow { section.resolve(invocationWithoutPerm.get(sender)) }
        assertThrows(ValidationCommandException::class.java) { section.resolve(invocationInnerPerm.get(sender)) }
        assertThrows(ValidationCommandException::class.java) { section.resolve(invocationMainPerm.get(sender)) }
    }

    @Test
    fun `test sender with all permission`() {
        val sender = TestPermissionSender()
            .permission("dev.rollczi.main")
            .permission("dev.rollczi.main.inner")

        assertDoesNotThrow { section.resolve(invocationMainPermOnly.get(sender)) }
        assertDoesNotThrow { section.resolve(invocationAllPerm.get(sender)) }
        assertDoesNotThrow { section.resolve(invocationWithoutPerm.get(sender)) }
        assertDoesNotThrow { section.resolve(invocationInnerPerm.get(sender)) }
        assertDoesNotThrow { section.resolve(invocationMainPerm.get(sender)) }
    }

    @Test
    fun `test sender with only main permission`() {
        val sender = TestPermissionSender()
            .permission("dev.rollczi.main")

        assertDoesNotThrow { section.resolve(invocationMainPermOnly.get(sender)) }
        assertThrows(ValidationCommandException::class.java) { section.resolve(invocationAllPerm.get(sender)) }
        assertDoesNotThrow { section.resolve(invocationWithoutPerm.get(sender)) }
        assertThrows(ValidationCommandException::class.java) { section.resolve(invocationInnerPerm.get(sender)) }
        assertDoesNotThrow { section.resolve(invocationMainPerm.get(sender)) }
    }

    @Test
    fun `test sender with only inner permission`() {
        val sender = TestPermissionSender()
            .permission("dev.rollczi.main.inner")

        assertThrows(ValidationCommandException::class.java) { section.resolve(invocationMainPermOnly.get(sender)) }
        assertThrows(ValidationCommandException::class.java) { section.resolve(invocationAllPerm.get(sender)) }
        assertDoesNotThrow { section.resolve(invocationWithoutPerm.get(sender)) }
        assertDoesNotThrow { section.resolve(invocationInnerPerm.get(sender)) }
        assertThrows(ValidationCommandException::class.java) { section.resolve(invocationMainPerm.get(sender)) }
    }

}