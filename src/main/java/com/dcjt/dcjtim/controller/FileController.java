package com.dcjt.dcjtim.controller;

import com.alibaba.fastjson.JSONObject;
import com.dcjt.dcjtim.bean.ChatProtocol;
import com.dcjt.dcjtim.bean.FileConfig;
import com.dcjt.dcjtim.bean.REnum;
import com.dcjt.dcjtim.bean.RWrapper;
import com.dcjt.dcjtim.entity.ChatRoom;
import com.dcjt.dcjtim.entity.ChatSole;
import com.dcjt.dcjtim.service.IChatRoomService;
import com.dcjt.dcjtim.service.IChatSoleService;
import com.dcjt.dcjtim.socket.session.TransmitAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Optional;

/**
 * <p>
 *
 * </p>
 *
 * @author 滔哥
 * @since 2020/7/29
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private static final String DEFAULT_PATH = new File("").getAbsoluteFile() + File.separator + "upload" + File.separator;

    @Autowired
    IChatSoleService soleService;
    @Autowired
    IChatRoomService roomService;
    @Autowired
    FileConfig fileConfig;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;


    @PostMapping("/upload")
    Object fileUpload(ChatProtocol chat, @RequestParam("file") MultipartFile file) {
        if (!chat.getTarget().isEmpty() && !file.isEmpty()) {
            chat.setTime(LocalDateTime.now());
            String fileName = file.getOriginalFilename();
            // 文件名
            String prefix = StringUtils.substringBeforeLast(fileName, ".");
            // 文件后缀
            String suffix = StringUtils.substringAfterLast(fileName, ".");

            if (null == suffix) {
                return RWrapper.failture("非法文件上传失败");
            }

            /* // 允许上传的文件格式
            boolean fileMatch = fileConfig.getFileSuffix().stream().anyMatch(s -> s.equals(suffix));
            if (!fileMatch){
                return RWrapper.failture("非法文件上传失败");
            }*/

            // 存储路径
            String filePath = new StringBuilder().append(File.separator)
                    .append(LocalDateTime.now().getYear()).append(File.separator)
                    .append(LocalDateTime.now().getMonthValue()).append(File.separator)
                    .append(prefix).append("_").append(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()).append(".").append(suffix)
                    .toString();

            String absolutePath = Optional.ofNullable(fileConfig.getSavePath()).isPresent() ? fileConfig.getSavePath() + filePath :
                    DEFAULT_PATH + filePath;
            chat.setMaterialName(fileName);
            chat.setMaterialId(filePath);

            try {
                File dir = new File(absolutePath);
                if (!dir.getParentFile().exists()) {
                    dir.getParentFile().mkdirs();
                }
                file.transferTo(dir);
            } catch (IOException e) {
                e.printStackTrace();
                return RWrapper.failture(REnum.SAVEFAILED);
            }
            sendAndSaveMsg(chat);

            return RWrapper.success();
        }
        return RWrapper.failture("目标端和目录不能为空");
    }

    @RequestMapping("/download")
    Object fileDownload(String materialId) {
        try {
            String filePath = new String(Base64.getDecoder().decode(materialId), StandardCharsets.UTF_8);
            String absolutePath = Optional.ofNullable(fileConfig.getSavePath()).isPresent() ? fileConfig.getSavePath() + filePath :
                    DEFAULT_PATH + filePath;
            String suffix = StringUtils.substringAfterLast(absolutePath, ".");
            String fileName = StringUtils.substringBeforeLast(absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1), "_")
                    + "." + suffix;

            File file = new File(absolutePath);
            if (file.exists()) {
                // 构建响应
                ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
                bodyBuilder.contentLength(file.length());
                // 二进制数据流
                bodyBuilder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
                bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);
                // 文件名解码
                String encodeFileName = URLEncoder.encode(fileName, "UTF-8");

                //在浏览器中直接打开
                /*URL url = new URL("file:///" + file);
                bodyBuilder.header(HttpHeaders.CONTENT_TYPE, url.openConnection().getContentType());*/

                // 直接下载
                bodyBuilder.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=UTF-8''" + encodeFileName);

                return bodyBuilder.body(FileUtils.readFileToByteArray(file));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return RWrapper.failture("请求文件已过期");
        }
        log.info("文件 {} 不存在", materialId);
        return RWrapper.failture("请求文件不存在");
    }

    @RequestMapping("/download2")
    void fileDownload(String materialId, HttpServletResponse response) {
        try {
            String filePath = new String(Base64.getDecoder().decode(materialId), StandardCharsets.UTF_8);
            String absolutePath = Optional.ofNullable(fileConfig.getSavePath()).isPresent() ? fileConfig.getSavePath() + filePath :
                    DEFAULT_PATH + filePath;
            String suffix = StringUtils.substringAfterLast(absolutePath, ".");
            String fileName = StringUtils.substringBeforeLast(absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1), "_")
                    + "." + suffix;
            File file = new File(absolutePath);
            if (file.exists()) {
                response.setHeader(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                // 下载文件能正常显示中文
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

                byte[] buffer = new byte[1024];
                try (FileInputStream fis = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(fis);
                     OutputStream os = response.getOutputStream()) {
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            } else {
                log.info("文件 {} 不存在", materialId);
            }
        } catch (Exception e) {
            log.info("文件 {} 不存在", materialId);
        }
    }

    private void sendAndSaveMsg(ChatProtocol chat) {
        String encoderPath=Base64.getEncoder().encodeToString(chat.getMaterialId().getBytes(StandardCharsets.UTF_8));
        if (chat.getGroup()) {
            ChatRoom room = new ChatRoom();
            BeanUtils.copyProperties(chat, room);
            roomService.save(room);
            chat.setId(room.getId());
            chat.setMaterialId(encoderPath);
            TransmitAction.room(chat);
        } else {
            ChatSole sole = new ChatSole();
            BeanUtils.copyProperties(chat, sole);
            soleService.save(sole);
            chat.setId(sole.getId());
            chat.setMaterialId(encoderPath);
            TransmitAction.sole(chat);
        }
    }
}
