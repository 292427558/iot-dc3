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

db = db.getSiblingDB("admin");
db = db.getSiblingDB("authorization");
if (!db.getUser("admin")) {
    db.createUser({
        user: "admin",
        pwd: "dc3",
        roles: [
            {
                role: "readWrite",
                db: "authorization",
            },
        ],
    });
}

db = db.getSiblingDB("admin");
if (!db.getUser("admin")) {
    db.createUser({
        user: "admin",
        pwd: "dc3",
        roles: [
            {
                role: "readWrite",
                db: "admin",
            },
        ],
    });
}

db = db.getSiblingDB("dc3");
if (!db.getUser("dc3")) {
    db.createUser({
        user: "dc3",
        pwd: "dc3",
        roles: [
            {
                role: "readWrite",
                db: "dc3",
            },
        ],
    });
}

if (db.createCollection("pointValue")) {
    db.pointValue.ensureIndex(
        {
            deviceId: 1,
            pointId: 1,
        },
        {
            name: "IX_device_point_id",
            unique: false,
            background: true,
        }
    );
    db.pointValue.ensureIndex(
        {
            originTime: 1,
        },
        {
            name: "IX_origin_time",
            unique: false,
            background: true,
        }
    );
}

if (db.createCollection("driverEvent")) {
    db.driverEvent.ensureIndex(
        {
            serviceName: 1,
        },
        {
            name: "IX_service_name",
            unique: false,
            background: true,
        }
    );
    db.driverEvent.ensureIndex(
        {
            originTime: 1,
        },
        {
            name: "IX_origin_time",
            unique: false,
            background: true,
        }
    );
}

if (db.createCollection("deviceEvent")) {
    db.deviceEvent.ensureIndex(
        {
            deviceId: 1,
            pointId: 1,
        },
        {
            name: "IX_device_point_id",
            unique: false,
            background: true,
        }
    );
    db.deviceEvent.ensureIndex(
        {
            originTime: 1,
        },
        {
            name: "IX_origin_time",
            unique: false,
            background: true,
        }
    );
}
