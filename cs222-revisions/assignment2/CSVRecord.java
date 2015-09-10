/*
 * This is a modification of the original source released by the Apache
 * Source Foundation.  The original license is listed below.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.csv;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @version $Id$
 */
public final class CSVRecord implements Serializable, Iterable<String> {
	
	public class Builder {
		private String[] assertStringArray();
		private Map<String, Integer> mapping;
		private String comment;
		private long recordNumber;
		private long characterPosition;
		
		/** The values of the record */
		public Builder withValues(String[] values) {
			this.values = assertStringArray(values);
			return this;
		}
		
		private assertStringArray(String[] strings) {
			if (strings == null) {
				strings = EMPTY_STRING_ARRAY;
			}
			return strings;
		}
		
		/**
		 *  The column name to index mapping.
		 */
		public Builder withMapping(Map<String, Integer> mapping) {
			this.mapping = mapping;
			return this;
		}
		
		/** 
		 * The accumulated comments (if any) 
		 */
		public Builder withComment(String comment) {
			this.comment = comment;
			return this;
		}
		
		/** The record number. */
		public Builder withRecordNumber(long recordNumber) {
			this.recordNumber = recordNumber;
			return this;
		}
		
		/**
		 * The character position of this record.
		 */
		public Builder withCharacterPosition(long characterPosition) {
			this.characterPosition = characterPosition;
			return this;
		}
		
		public CSVRecord build() {
			return new CSVRecord(this);
		}
	}

    private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final long serialVersionUID = 1L;

	private final Map<String, Integer> mapping;
	
    public final long characterPosition;
    public final String comment;
    public final long recordNumber;
    public final String[] values;

    private CSVRecord(Builder builder) {
    	mapping = builder.mapping;
    	characterPosition = builder.characterPosition;
    	comment = builder.comment;
    	recordNumber = builder.recordNumber;
    	values = builder.values;
    }
	
    public String getByEnum(final Enum<?> e) {
        return get(e.toString());
    }

    public String getByColumnIndex(final int index) {
        return values[index];
    }

    /**
     * Returns a value by name.
     *
     * @param name
     *            the name of the column to be retrieved.
     * @return the column value, maybe null depending on {@link CSVFormat#getNullString()}.
     * @throws IllegalStateException
     *             if no header mapping was provided
     * @throws IllegalArgumentException
     *             if {@code name} is not mapped or if the record is inconsistent
     * @see #isConsistent()
     * @see CSVFormat#withNullString(String)
     */
    public String getByColumnName(final String name) {
        if (mapping == null) {
            throw new IllegalStateException(
                "No header mapping was specified, the record values can't be accessed by name");
        }
        final Integer index = mapping.get(name);
        if (index == null) {
            throw new IllegalArgumentException(String.format("Mapping for %s not found, expected one of %s", name,
                mapping.keySet()));
        }
        try {
            return values[index.intValue()];
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format(
                "Index for header '%s' is %d but CSVRecord only has %d values!", name, index,
                Integer.valueOf(values.length)));
        }
    }

    /**
     * Tells whether the record size matches the header size.
     *
     * @return true of this record is valid, false if not. Some programs can export files that fail this
     * test but still produce parsable files.
     */
    public boolean isConsistent() {
        return mapping == null || mapping.size() == values.length;
    }

    public boolean isMapped(final String name) {
        return mapping != null && mapping.containsKey(name);
    }

    /**
     * Checks whether a given columns is mapped and has a value.
     *
     * @param name
     *            the name of the column to be retrieved.
     * @return whether a given columns is mapped and has a value
     */
    public boolean isSet(final String name) {
        return isMapped(name) && mapping.get(name).intValue() < values.length;
    }

    @Override
    public Iterator<String> iterator() {
        return toList().iterator();
    }

    /**
     * Puts all values of this record into the given Map.
     *
     * @param map
     *            The Map to populate.
     * @return the given map.
     */
    <M extends Map<String, String>> M putIn(final M map) {
        if (mapping == null) {
            return map;
        }
        for (final Entry<String, Integer> entry : mapping.entrySet()) {
            final int col = entry.getValue().intValue();
            if (col < values.length) {
                map.put(entry.getKey(), values[col]);
            }
        }
        return map;
    }

    public int size() {
        return values.length;
    }

    /**
     * Copies this record into a new Map. The new map is not connected to the Record object, so making changes to it
	 * will not affect this record.
     *
     * @return A new Map. The map is empty if the record has no headers.
     */
    public Map<String, String> toMap() {
        return putIn(new HashMap<String, String>(values.length));
    }

    /**
     * Returns a string representation of the contents of this record. The result is constructed by comment, mapping,
     * recordNumber and by passing the internal values array to {@link Arrays#toString(Object[])}.
     */
    @Override
    public String toString() {
        return "CSVRecord [comment=" + comment + ", mapping=" + mapping +
                ", recordNumber=" + recordNumber + ", values=" +
                Arrays.toString(values) + "]";
    }

}