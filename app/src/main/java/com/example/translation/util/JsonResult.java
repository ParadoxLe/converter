package com.example.translation.util;

import java.util.List;

//文字识别结果实体Bean
public class JsonResult {
    private long log_id;
    private int direction;
    private int words_result_num;
    private List<WordsResultBean> words_result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public List<WordsResultBean> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResultBean> words_result) {
        this.words_result = words_result;
    }

    public static class WordsResultBean {
        /**
         * words : 突然间有想看书的冲动
         */

        private String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }
}
