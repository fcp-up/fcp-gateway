package com.dfkj.fcp.protocol.hardware.mina;

import java.util.Date;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.vo.DataMessage;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.protocol.hardware.parse.Function;

/**
 * 硬件消息报文处理
 *
 * Created by JiangWenGuang on 2017/4/22.
 */
public class HardwareMessageFilter extends IoFilterAdapter {

    /**
     * 查找时间数据 条件
     */
    private static Function<DataMessageItem, EDeviceType, Boolean> TIME_WHERE = new Function<DataMessageItem, EDeviceType, Boolean>() {

        public Boolean apply(DataMessageItem t1, EDeviceType t2) {
            return (t1.getDevType() == t2);
        }

    };

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        if (message instanceof DataMessage) {
            DataMessage msg = (DataMessage)receiveMessage(session, (DataMessage) message);
            nextFilter.messageReceived(session, msg);
        }else{
            super.messageReceived(nextFilter, session, message);
        }

    }

//    @Override
//    public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
//
//        WriteFuture writeFuture = new DefaultWriteFuture(session);
//        WriteRequest _writeRequest = new DefaultWriteRequest(writeRequest.getMessage(), writeFuture, null);
//        nextFilter.filterWrite(session, _writeRequest);
//
//    }

    public DataMessage receiveMessage(IoSession session, DataMessage data) {

        if (data instanceof DataMessage) {
            DataMessage dataMessage = (DataMessage)data;
            if (dataMessage.getMsgType() == EMessageType.SENSOR_DATA) {
                DataMessageItem timeItem = dataMessage.getData().single(TIME_WHERE, EDeviceType.TIME);

                //	移除时间
                dataMessage.getData().delete(TIME_WHERE, EDeviceType.TIME);

                /**
                 * 更新传感器数据中的时间
                 * 如果设备上传的数据中没有时间字段，则使用当前时间进行更新
                 * songfei 修改 2016-06-21
                 */
                EValueStatus valStatus = EValueStatus.VALID;
                Date now = new Date();
                Date itemDate = now;
                if (timeItem != null) {
                    /**
                     * 还需要进行判断，上传数据的时间是否有效
                     */
                    itemDate = timeItem.getDate();

                    if (itemDate.getTime() < 0) {
                        valStatus = EValueStatus.INVALID_TIME;
                    }
                    else {
                        long milliseconds = now.getTime() - itemDate.getTime();
                        long seconds = milliseconds / 1000;
                        /**
                         * 大于180的数据认为无效
                         */
                        if (seconds > (180 * 24 * 60 * 60)) {
                            itemDate = null;
                            valStatus = EValueStatus.INVALID_TIME;
                        }
                    }
                }

                for (DataMessageItem item : dataMessage.getData()) {
                    item.setDate(itemDate);
                    if (valStatus == EValueStatus.INVALID_TIME) {
                        item.setValStatus(valStatus);
                    }
                }
            }
        }

       return data;
    }

}
