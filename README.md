# Secrets

Simple Secret Sharing Service for social and decentralised management of passwords

[![software by Dyne.org](https://www.dyne.org/wp-content/uploads/2015/12/software_by_dyne.png)](http://www.dyne.org)

Free and fully functional demo on [secrets.dyne.org](https://secrets.dyne.org)

[![Build Status](https://travis-ci.org/PIENews/secrets.svg?branch=master)](https://travis-ci.org/PIENews/secrets)

Secrets uses the underlying cryptographic protocol developed in [Freecoin](https://github.com/PIENews/freecoin). It is used to split pins into pieces to be distributed to friends, so that when the pin is lost it can be recuperated by putting together the pieces. Secret sharing can have many other uses, depending from the context in which it is deployed and this tool aims at being a very simple and highly available implementation free for anyone to use.

The FXC protocol and its use cases related to social digital currency are explained in detail in the deliverable [Implementation of digital social currency infrastructure (D5.5)](http://dcentproject.eu/wp-content/uploads/2015/10/D5.5-Implementation-of-digital-social-currency-infrastructure-.pdf) produced as part of the research conducted in the [D-CENT project](http://dcentproject.eu/resource_category/publications/).



## Running Secrets on your own computer


<img align="right" src="resources/public/static/img/clojure.png">

Secrets is written in Clojure and is fully cross-platform: one can run it locally on a GNU/Linux machine, as well on Apple/OSX and MS/Windows.

<img align="left" src="http://leiningen.org/img/leiningen.jpg">

The following software is required:

 - [OpenJDK](http://openjdk.java.net)
 - [Clojure](http://clojure.org)
 - [Leiningen](http://leiningen.org)

For instance on Devuan systems one can install all necessary dependencies via the following packages:

```
wget openjdk-7-jdk libversioneer-clojure haveged
```

then install Leiningen which will take care of all Clojure dependencies:

```
mkdir ~/bin
wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein -O ~/bin/lein
chmod +x ~/bin/lein
```

then from inside the Secrets source, start it with:

```
lein ring server
```

This command will open a browser pointing on the service running on localhost port 8000

To start only a web server for the application, but no browser, run: `server-headless`

## Specs

Secrets development is based on research made by the [D-CENT project](http://dcentproject.eu) (Decentralized Citizen Engagement Technologies), R&D funded by the European Commission (FP7/CAPS 610349).

Based on the Shamir Secret Sharing algorithm by Adi Shamir
 - Shamir, Adi (1979), "How to share a secret", Communications of the ACM 22 (11): 612–613
 - Knuth, D. E. (1997), The Art of Computer Programming, II: Seminumerical Algorithms: 505

Implemented using the Secret Share Java library by Tim Tiemens with a 4096 ciphers prime number.

Industry standards:
 - [ISO/IEC 19592-1:2016](https://www.iso.org/standard/65422.html) Information technology -- Security techniques -- Secret sharing -- Part 1: General
 - [ISO/IEC FDIS 19592-2 (Under development)](https://www.iso.org/standard/65425.html)  Information technology -- Security techniques -- Secret sharing -- Part 2: Fundamental mechanisms



## License

Secrets is Copyright (C) 2015-2017 by the Dyne.org Foundation

Software Designed, written and maintained by Denis Roio <jaromil@dyne.org>

```
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```
