package org.rapidoid.io;

/*
 * #%L
 * rapidoid-commons
 * %%
 * Copyright (C) 2014 - 2016 Nikolche Mihajlovski and contributors
 * %%
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
 * #L%
 */

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.commons.Str;
import org.rapidoid.u.U;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

@Authors("Nikolche Mihajlovski")
@Since("5.2.4")
public class FileSearch extends RapidoidThing {

	private volatile String[] name;

	private volatile String[] ignore;

	private volatile String regex;

	private volatile String ignoreRegex;

	private volatile String location;

	private volatile boolean files;

	private volatile boolean folders;

	private volatile boolean recursive;

	public FileSearch name(String... name) {
		this.name = name;
		return this;
	}

	public FileSearch ignore(String... ignore) {
		this.ignore = ignore;
		return this;
	}

	public FileSearch in(String location) {
		this.location = location;
		return this;
	}

	public FileSearch regex(String regex) {
		this.regex = regex;
		return this;
	}

	public FileSearch ignoreRegex(String ignoreRegex) {
		this.ignoreRegex = ignoreRegex;
		return this;
	}

	public FileSearch files() {
		this.files = true;
		return this;
	}

	public FileSearch folders() {
		this.folders = true;
		return this;
	}

	public FileSearch recursive() {
		this.recursive = true;
		return this;
	}

	public List<String> getNames() {
		List<String> names = U.list();

		for (File file : get()) {
			names.add(file.getAbsolutePath());
		}

		return names;
	}

	public List<String> getRelativeNames() {
		List<String> names = U.list();

		for (String filename : getNames()) {
			U.must(filename.startsWith(location));

			String relative = Str.triml(filename, location);
			relative = Str.triml(relative, File.separator);

			names.add(relative);
		}

		return names;
	}

	public List<File> get() {
		List<File> found = U.list();

		boolean filesAndFolders = files == folders;

		U.must(U.notEmpty(location), "Location must be specified!");

		U.must(U.isEmpty(name) || U.isEmpty(regex), "You can specify either 'name' or 'regex', not both of them!");
		U.must(U.isEmpty(ignore) || U.isEmpty(ignoreRegex), "You can specify either 'ignore' or 'ignoreRegex', not both of them!");

		String matching = U.notEmpty(name) ? Str.wildcardsToRegex(name) : regex;
		String ignoring = U.notEmpty(ignore) ? Str.wildcardsToRegex(ignore) : ignoreRegex;

		Pattern mtch = U.notEmpty(matching) ? Pattern.compile(matching) : null;
		Pattern ignr = U.notEmpty(ignoring) ? Pattern.compile(ignoring) : null;

		search(new File(location), found, mtch, ignr, files || filesAndFolders, folders || filesAndFolders, recursive);

		return found;
	}

	static void search(File dir, List<File> found, Pattern matching, Pattern ignoring,
	                   boolean includeFiles, boolean includeDirectories, boolean recursive) {

		File[] files = dir.listFiles();

		if (files != null) {
			for (File f : files) {

				if ((includeFiles && f.isFile()) || (includeDirectories && f.isDirectory())) {

					String filename = f.getAbsolutePath();

					if (matching == null || matching.matcher(filename).matches()) {
						if (ignoring == null || !ignoring.matcher(filename).matches()) {
							found.add(f);
						}
					}
				}

				if (recursive && f.isDirectory()) {
					search(f, found, matching, ignoring, includeFiles, includeDirectories, recursive);
				}
			}
		}
	}

}
