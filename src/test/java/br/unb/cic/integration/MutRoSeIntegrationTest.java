package br.unb.cic.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest
public class MutRoSeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String getContent(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/main/resources/testFiles/MutRoSe" + path)));
    }

    @Test
    public void contextLoad() throws Exception {
        // Test to start the application
        testCase0();
//        testCase1();
//        testCase2();
//        testCase3();
//        testCase4();
//        testCase5();
//        testCase6();
//        testCase7();
    }

    public void testCase0() throws Exception {
        String content = getContent("model.txt");
        try {
            mockMvc.perform(post("/load/multrose").param("content", content))
            		.andDo(print())
                    .andExpect(status().isOk());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    public void testCase1() throws Exception {
        String content = getContent("model.txt");
        try {
            mockMvc.perform(post("/load/multrose").param("content", content))
            		.andDo(print())
                    .andExpect(status().isOk());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    public void testCase2() throws Exception {
        String content = getContent("model.txt");
        try {
            mockMvc.perform(post("/prism/MDP").param("content", content))
            		.andDo(print())
                    .andExpect(status().isOk());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    public void testCase3() throws Exception {
        String content = getContent("model.txt");
        try {
            mockMvc.perform(post("/prism/MDP").param("content", content))
            		.andDo(print())
                    .andExpect(status().isOk());
        } catch (Exception e) {
            Assert.fail();
        }
    }
    public void testCase4() throws Exception {
        String content = getContent("Test4.txt");
        try {
            mockMvc.perform(post("/prism/MDP").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    public void testCase5() throws Exception {
        String content = getContent("Test5.txt");
        try {
            mockMvc.perform(post("/prism/MDP").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    public void testCase6() throws Exception {
        String content = getContent("Test6.txt");
        try {
            mockMvc.perform(post("/prism/MDP").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    public void testCase7() throws Exception {
        String content = getContent("Test7.txt");
        try {
            mockMvc.perform(post("/prism/MDP").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

}
