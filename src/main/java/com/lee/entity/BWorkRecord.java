package com.lee.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    public class BWorkRecord implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * ID
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 用户ID
     */
      private Integer uid;

      /**
     * 打卡日期
     */
      private String workDate;

      /**
     * 上班时间
     */
      private String upTime;

      /**
     * 下班时间
     */
      private String downTime;

      /**
     * 打卡状态
     */
      private Integer status;


      /**
     * 创建时间
     */
      @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;


      /**
     * 更新时间
     */
      @TableField(fill = FieldFill.INSERT)

      private LocalDateTime updateTime;


}
