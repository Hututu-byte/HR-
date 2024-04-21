package com.lee.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author admin
 * @since 2024-04-14
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class BMerit implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * ID
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;


      /**
     * 考核时间
     */
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//转类型
      private LocalDateTime setDate;

      /**
     * 月考核量
     */
      private String monthQuota;

      /**
     * 周考核量
     */
      private String weekQuota;


}
