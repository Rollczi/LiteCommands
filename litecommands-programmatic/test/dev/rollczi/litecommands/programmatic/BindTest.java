package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestPlatformSender;
import dev.rollczi.litecommands.unit.TestSender;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BindTest {

    static class ServiceHolder {
        private String name;

        public ServiceHolder(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    ServiceHolder serviceHolder = new ServiceHolder("-");
    TestPlatform testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder
        .bind(ServiceHolder.class, () -> serviceHolder)
        .commands(
            new LiteCommand<TestSender>("test")
                .bind("service", ServiceHolder.class)
                .execute(context -> {
                    ServiceHolder service = context.bind("service", ServiceHolder.class);
                    service.setName("success");
                })
        ));

    @Test
    @DisplayName("Should provide service")
    public void testSuccess() {
        serviceHolder.setName("start");
        testPlatform.execute(TestPlatformSender.named("user:Rollczi"),"test")
            .assertSuccess();

        assertThat(serviceHolder.getName()).isEqualTo("success");
    }


}
