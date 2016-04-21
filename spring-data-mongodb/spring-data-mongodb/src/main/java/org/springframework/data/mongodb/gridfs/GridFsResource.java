/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mongodb.gridfs;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import com.mongodb.gridfs.GridFSDBFile;

/**
 * {@link GridFSDBFile} based {@link Resource} implementation.
 * 
 * @author Oliver Gierke
 */
public class GridFsResource extends InputStreamResource {

	private final GridFSDBFile file;

	/**
	 * Creates a new {@link GridFsResource} from the given {@link GridFSDBFile}.
	 * 
	 * @param file must not be {@literal null}.
	 */
	public GridFsResource(GridFSDBFile file) {
		super(file.getInputStream());
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.io.AbstractResource#contentLength()
	 */
	@Override
	public long contentLength() throws IOException {
		return file.getLength();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.io.AbstractResource#getFilename()
	 */
	@Override
	public String getFilename() throws IllegalStateException {
		return file.getFilename();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.io.AbstractResource#lastModified()
	 */
	@Override
	public long lastModified() throws IOException {
		return file.getUploadDate().getTime();
	}

	/**
	 * Returns the {@link Resource}'s id.
	 * 
	 * @return
	 */
	public Object getId() {
		return file.getId();
	}

	/**
	 * Returns the {@link Resource}'s content type.
	 * 
	 * @return
	 */
	public String getContentType() {
		return file.getContentType();
	}
}
