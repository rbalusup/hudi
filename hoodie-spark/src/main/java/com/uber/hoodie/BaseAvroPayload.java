/*
 *  Copyright (c) 2017 Uber Technologies, Inc. (hoodie-dev-group@uber.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package com.uber.hoodie;

import com.uber.hoodie.common.util.HoodieAvroUtils;
import com.uber.hoodie.exception.HoodieException;
import com.uber.hoodie.exception.HoodieIOException;
import java.io.IOException;
import java.io.Serializable;
import org.apache.avro.generic.GenericRecord;

/**
 * Base class for all AVRO record based payloads, that can be ordered based on a field
 */
public abstract class BaseAvroPayload implements Serializable {


  /**
   * Avro data extracted from the source converted to bytes
   */
  protected final byte [] recordBytes;

  /**
   * The schema of the Avro data
   */
  protected final String schemaStr;

  /**
   * For purposes of preCombining
   */
  protected final Comparable orderingVal;

  /**
   * @param record
   * @param orderingVal
   */
  public BaseAvroPayload(GenericRecord record, Comparable orderingVal) {
    try {
      this.recordBytes = HoodieAvroUtils.avroToBytes(record);
      this.schemaStr = record.getSchema().toString();
    } catch (IOException io) {
      throw new HoodieIOException("Cannot convert GenericRecord to bytes", io);
    }
    this.orderingVal = orderingVal;
    if (orderingVal == null) {
      throw new HoodieException("Ordering value is null for record: " + record);
    }
  }
}
