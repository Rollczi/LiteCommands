package dev.rollczi.litecommands.jda;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JDATestSpec {

    @Mock
    protected JDA jda;

    @Mock
    protected User user;

    @BeforeEach
    void beforeEach() {
        when(jda.upsertCommand(any())).thenReturn(mock());
        when(jda.retrieveCommands()).thenReturn(mock());
        when(user.openPrivateChannel()).thenReturn(mock());
    }

}
