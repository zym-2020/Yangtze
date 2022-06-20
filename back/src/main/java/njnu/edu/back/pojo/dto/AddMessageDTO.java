package njnu.edu.back.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMessageDTO {

        String id;
        String dataName;
        String messageType;
        String dataUploadTime;
        String dataExamineTime;
        String dataCache;
        String MessageRequest;
        String reply;
        String fileId;
        String messageSender;
        String messageReceiver;
        String messageResponse;
        String replyUser;


}
