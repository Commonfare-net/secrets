# Simple Secret Sharing

<a href="https://www.dyne.org"><img
	src="https://secrets.dyne.org/static/img/swbydyne.png"
		alt="software by Dyne.org"
			title="software by Dyne.org" class="pull-right"></a>

## Social and decentralised management of secrets


<h2>Demo: <a href="https://secrets.dyne.org/share">secrets.dyne.org/share</a></h2>

<a href="https://secrets.dyne.org/share">
	<img src="https://secrets.dyne.org/static/img/secret_ladies.jpg"
		title="try it online" alt="use secrets online" style="overflow: hidden">

[![Build Status](https://travis-ci.org/PIENews/secrets.svg?branch=master)](https://travis-ci.org/PIENews/secrets)

Secrets can be used to split a secret (like a password) into pieces to be distributed to friends. When all friends agree, the pieces can be recombined to retrieve the original secret password, for instance to give consensual access to a private document or service.

Secret sharing can be useful in many different situations and this tool is a very simple and highly available implementation of secret sharing, free for anyone to use, locally installed or from this website.

Secrets uses the underlying "FXC" cryptographic protocol developed in [Freecoin](https://github.com/PIENews/freecoin). The FXC protocol and its use cases related to social digital currency are explained in detail in the deliverable [Implementation of digital social currency infrastructure (D5.5)](http://dcentproject.eu/wp-content/uploads/2015/10/D5.5-Implementation-of-digital-social-currency-infrastructure-.pdf) produced as part of the research conducted in the [D-CENT project](http://dcentproject.eu/resource_category/publications/).



## Running Secrets on your own computer


<img class="pull-right" src="https://secrets.dyne.org/static/img/clojure.png">

Secrets is written in Clojure and is fully cross-platform: one can run it locally on a GNU/Linux machine, as well on Apple/OSX and MS/Windows.

<img class="pull-left" src="http://secrets.dyne.org/static/img/leiningen.jpg"
	style="padding-right: 1.5em">

The following software is required: [OpenJDK](http://openjdk.java.net), [Clojure](http://clojure.org), [Leiningen](http://leiningen.org).

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

## Acknowledgments

Secrets is Free and Open Source research and development activity funded by the European Commission in the context of the [Collective Awareness Platforms for Sustainability and Social Innovation (CAPSSI)](https://ec.europa.eu/digital-single-market/en/collective-awareness) program. It was developed within the [Decentralised Citizen Engagement Technologies](http://dcentproject.eu) project (grant nr. 610349) and further adopted and maintained by the [Commonfare](http://pieproject.eu) project (grant nr. 687922).

The [Secret Sharing](https://en.wikipedia.org/wiki/Secret_sharing) algorithm adopted is based on [Shamir's Secret Sharing](https://en.wikipedia.org/wiki/Shamir%27s_Secret_Sharing), references:
- Shamir, Adi (1979), "How to share a secret", Communications of the ACM 22 (11): 612–613
- Knuth, D. E. (1997), The Art of Computer Programming, II: Seminumerical Algorithms: 505
The implementation used is by Tim Tiemens with a 4096 cipher prime number.

The Integer Compression algorithm used internally is the `FastPFOR128` by Daniel Lemire, see: Lemire, D. and Boytsov, L. "[Decoding billions of integers per second through vectorization](http://arxiv.org/abs/1209.2137)" (2015).

Industry standard addressed: Information technology -- Security techniques -- Secret sharing
- [ISO/IEC 19592-1:2016](https://www.iso.org/standard/65422.html) (Part 1: General)
- [ISO/IEC FDIS 19592-2 (Under development)](https://www.iso.org/standard/65425.html) (Part 2: Fundamental mechanisms)


## License

Secrets is Copyright (C) 2015-2017 by the Dyne.org Foundation

Secrets and the FXC "simple secret sharing" protocol and library are designed, written and maintained by Denis Roio <jaromil@dyne.org>

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
