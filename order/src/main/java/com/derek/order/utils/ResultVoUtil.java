package com.derek.order.utils;

import com.derek.order.vo.ResultVO;

public class ResultVoUtil {

    public static ResultVO success(Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMessage("成功");
        resultVO.setData(object);
        return resultVO;
    }
}
