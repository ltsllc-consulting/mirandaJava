package com.ltsllc.mirandajava;


import com.ltsllc.miranda.clientinterface.Results;

/**
 * Created by Clark on 6/7/2017.
 */
public class ResponseObject {
    private Results result;
    private Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Results getResult() {

        return result;
    }

    public void setResult(Results result) {
        this.result = result;
    }
}
