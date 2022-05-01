package br.unb.cic.goda.rtgoretoprism.generator;

public class ManageCMD {
    public static ResultCMD executeCommand(String command) {
        ResultCMD resultCMD = new ResultCMD();
        try {
            int c = 0;
            Process exec = Runtime.getRuntime().exec(command);
            StringBuilder error = new StringBuilder();
            while ((c = exec.getErrorStream().read()) != -1) {
                error.append((char) c);
            }

            String errorString = error.toString();
            if(!errorString.isEmpty() && errorString.contains("what():")){
                errorString = errorString.substring(errorString.indexOf("what():") + 7, errorString.length());
            }

            c = 0;
            StringBuilder result = new StringBuilder();
            while ((c = exec.getInputStream().read()) != -1) {
                result.append((char) c);
            }

            resultCMD.setResult(result.toString());
            resultCMD.setError(errorString);
            return resultCMD;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static class ResultCMD{
        private String result;
        private String error;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}


