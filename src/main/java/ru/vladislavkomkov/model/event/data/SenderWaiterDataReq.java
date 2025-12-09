package ru.vladislavkomkov.model.event.data;

public class SenderWaiterDataReq
{
    private String key;
    private Object data;

    public SenderWaiterDataReq(String key, Object data)
    {
        this.key = key;
        this.data = data;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
