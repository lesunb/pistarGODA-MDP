package br.unb.cic.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MutRoSeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String getContent(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/main/resources/testFiles/MutRoSe" + path)));
    }

    @Test
    public void contextLoad() throws Exception {
        // Test to start the application
        testCase1();
    }

    @Test
    public void testCase1() throws Exception {
        String content = getContent("Test1.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
            		.andDo(print())
                    .andExpect(status().isOk());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCase2() throws Exception {
        String content = getContent("Test2.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCase3() throws Exception {
        String content = getContent("Test3.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase4() throws Exception {
        String content = getContent("Test4.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase5() throws Exception {
        String content = getContent("Test5.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase6() throws Exception {
        String content = getContent("Test6.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase7() throws Exception {
        String content = getContent("Test7.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase8() throws Exception {
        String content = getContent("Test8.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase9() throws Exception {
        String content = getContent("Test9.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase10() throws Exception {
        String content = getContent("Test10.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase11() throws Exception {
        String content = getContent("Test11.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase12() throws Exception {
        String content = getContent("Test12.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase13() throws Exception {
        String content = getContent("Test13.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase14() throws Exception {
        String content = getContent("Test14.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase15() throws Exception {
        String content = getContent("Test15.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase16() throws Exception {
        String content = getContent("Test16.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase17() throws Exception {
        String content = getContent("Test17.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase18() throws Exception {
        String content = getContent("Test18.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase19() throws Exception {
        String content = getContent("Test19.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase20() throws Exception {
        String content = getContent("Test20.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase21() throws Exception {
        String content = getContent("Test21.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase22() throws Exception {
        String content = getContent("Test22.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase23() throws Exception {
        String content = getContent("Test23.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase24() throws Exception {
        String content = getContent("Test24.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase25() throws Exception {
        String content = getContent("Test25.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase26() throws Exception {
        String content = getContent("Test26.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase27() throws Exception {
        String content = getContent("Test27.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase28() throws Exception {
        String content = getContent("Test28.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase29() throws Exception {
        String content = getContent("Test29.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase30() throws Exception {
        String content = getContent("Test30.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase31() throws Exception {
        String content = getContent("Test31.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase32() throws Exception {
        String content = getContent("Test32.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase33() throws Exception {
        String content = getContent("Test33.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase34() throws Exception {
        String content = getContent("Test34.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testCase35() throws Exception {
        String content = getContent("Test35.txt");
        try {
            mockMvc.perform(post("/prism-dtmc").param("content", content))
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

}
