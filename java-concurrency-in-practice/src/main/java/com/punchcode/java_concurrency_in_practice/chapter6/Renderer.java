package com.punchcode.java_concurrency_in_practice.chapter6;

import com.punchcode.java_concurrency_in_practice.chapter6.common.ImageData;
import com.punchcode.java_concurrency_in_practice.chapter6.common.ImageInfo;

import java.util.List;
import java.util.concurrent.*;

import static com.punchcode.java_concurrency_in_practice.chapter5.Preloader.launderThrowable;
import static com.punchcode.java_concurrency_in_practice.chapter6.common.ImageData.renderImage;
import static com.punchcode.java_concurrency_in_practice.chapter6.common.ImageInfo.renderText;
import static com.punchcode.java_concurrency_in_practice.chapter6.common.ImageInfo.scanForImageInfo;

/**
 * @author huanruiz
 * @since 2022/3/14
 */
public class Renderer {
    
    private final ExecutorService executor;
    
    Renderer(ExecutorService executor) {
        this.executor = executor;
    }
    
    void renderPage(CharSequence source) {
        List<ImageInfo> info = scanForImageInfo(source);
        CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executor);
        
        for (final ImageInfo imageInfo : info) {
            // 下载任务拆线程做
            completionService.submit(new Callable<ImageData>() {
                @Override
                public ImageData call() {
                    return imageInfo.downloadImage();
                }
            });
        }
    
        renderText(source);
    
        try {
            for (int t = 0, n = info.size(); t < n; t++) {
                // 把已经下载成功的图片分别渲染, 消费阻塞队列
                Future<ImageData> f = completionService.take();
                ImageData imageData = f.get();
                renderImage(imageData);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw launderThrowable(e.getCause());
        }
    }
}
