package com.ibm.marketplace.checkout.orderhub.ordersubmit.messagehub;

/**
 * data bean of order msg to BMX message hub
 * @author zgsun
 *
 */
public class OrderMessage {
	private String msgSrc; //order source: renewal, BP Q2O, QTM, 3PM etc
	private String msgType;// order type: DSW, GAIA
	private long msgID;//unique ID of the msg, use the msg timestamp here
	private String orderDocID;//cloudant document ID

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public long getMsgID() {
		return msgID;
	}

	public void setMsgID(long msgID) {
		this.msgID = msgID;
	}

	public String getOrderDocID() {
		return orderDocID;
	}

	public void setOrderDocID(String orderDocID) {
		this.orderDocID = orderDocID;
	}

	public String getMsgSrc() {
		return msgSrc;
	}

	public void setMsgSrc(String msgSrc) {
		this.msgSrc = msgSrc;
	}
	
    @Override
    public String toString() {
        String key = this.msgSrc == null ? "null" : this.msgSrc.toString();
        String value = this.msgType == null ? "null" : this.msgType.toString();
        String orderDoc = this.orderDocID == null ? "null" : this.orderDocID.toString();
        return "ProducerRecord(" + key + "," + value + "," + orderDoc +")";
    }

}
