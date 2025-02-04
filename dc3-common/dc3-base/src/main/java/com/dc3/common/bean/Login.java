/*
 * Copyright 2016-2021 Pnoker. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dc3.common.bean;

import com.dc3.common.valid.Auth;
import com.dc3.common.valid.Check;
import com.dc3.common.valid.Update;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * Login
 *
 * @author pnoker
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Login {

    @NotBlank(message = "tenant can't be empty", groups = {Auth.class})
    private String tenant;

    @NotBlank(message = "name can't be empty", groups = {Check.class, Auth.class, Update.class})
    private String name;

    @NotBlank(message = "salt can't be empty", groups = {Check.class, Auth.class})
    private String salt;

    @NotBlank(message = "password can't be empty", groups = {Auth.class})
    private String password;

    @NotBlank(message = "token can't be empty", groups = {Check.class})
    private String token;

}
