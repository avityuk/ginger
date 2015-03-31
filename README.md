ginger
======

ginger is a simple type safe i18n library for Java. Inspired by ideas from [GWT intenalization features](https://developers.google.com/web-toolkit/doc/latest/DevGuideI18n).

**It is currently in development with version 0.2.1.**

Features
--------
- Type safety. Inspired by GWT messages and constants
- Supports standard Java properties with UTF-8 encoding
- Pluralization support for about 150 languages 
- Servlet and JSP support
- Easy Spring integration
- [Joda Time](joda-time.sourceforge.net) support in message format
- Extensibility. Any file format can be used for loading localization resources from any location
- Easy to use, fast and thread-safe
- [Commons Lang 3](http://commons.apache.org/lang/)  is the only single dependency.


FAQ
---
### My application does not require localization, should I use it?
Even if you are not using localization aspect of ginger you can still benefit from it.
- Externalizing strings and messages is a good practice for any application.
- It can be used as an application wide holder for formatters: dates, numbers and so on. In contrast to regular formatters it motivates to localize them in single place + it solves JDK formatters thread-safety issues.
- See [other features](#Features)

### I don't want to use Joda Time. Do I need to have it as a dependency?
No, you don't have to. ginger automatically enables Joda Time support only when it sees it in classpath.

### Is pluralization for language X supported?
Yes, if it is in [this list](http://www.unicode.org/cldr/charts/supplemental/language_plural_rules.html).

### Pluralization for language X is not supported. How can I add it?
Submit issue for your language with plural rules provided. See examples [here](http://www.unicode.org/cldr/charts/supplemental/language_plural_rules.html).

Roadmap
--------
- Make error handling and defaults more configurable
- YAML support for localization resources

Versioning
----------
Releases will be numbered with the following format:

    <major>.<minor>.<patch>

And constructed with the following guidelines:
- Breaking backward compatibility bumps the major (and resets the minor and patch)
- New additions without breaking backward compatibility bumps the minor (and resets the patch)
- Bug fixes and misc changes bumps the patch
- For more information on SemVer, please visit http://semver.org/.

How to contribute
------------
- Submit bug reports and feature requests
- Fork and create pull request on Github

License
-------

    Copyright 2012 Andriy Vityuk

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
