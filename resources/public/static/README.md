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

[![Code Climate](https://codeclimate.com/github/PIENews/secrets.png)](https://codeclimate.com/github/PIENews/secrets)

Secrets can be used to split a secret text into shares to be distributed to friends. When all friends agree, the shares can be combined to retrieve the original secret text, for instance to give consensual access to a lost pin, a password, a list of passwords, a private document or a key to an encrypted volume.

Secret sharing can be useful in many different situations and this tool is a simple and well documented free and open source implementation available for anyone to use from this website, but also independently on an offline PC.

## How to use Secrets

<div class="well well-sm">
1) Have a secret
</div>

For Secrets to be useful... one needs to have a secret :) don't be silly now, everyone has secrets. Lets say a backup of your passwords or a crypto wallet or a testament... remember it has to be just text and smaller than 1024 characters.

<div class="well well-sm">
2) Trust a group of people
</div>

Then you need to have 5 trusted friends or colleagues (lets say trusted peers) who can agree on the need to access your secrets in certain circumstances, for your own well being or that of your family or organisation.

<div class="well well-sm">
3) Paste the secret and distribute the shares
</div>

Clicking on [Share Secrets](/share) you can paste the secret in the text form and click submit. In the blink of an eye our software will give you long strings of numbers and letters that can be distributed to all 5 trusted peers. Its just a text string so you can decide the best medium to transmit it, its also rather easy to dictate.

<div class="well well-sm">
4) Explain to your peers what the secret is for
</div>

Take care to explain well the reason you are sharing this secret to all your trusted peers and the condition under which they shall meet again and combine all the shares to access the secret, which will be unknown to them until that moment. You need to trust them to respect your will on this, as they could also disregard the conditions and access your secret without your will.

<div class="well well-sm">
5) Peers decide that your condition is met
</div>

Among the trusted peers holding your shared secret at least 3 can decide at any time that your conditions to retreive the secret are met. Please note that just 3 out of 5 are enough, which insures the availability of the secret even in case 2 peers are unavailable for some reason.

<div class="well well-sm">
6) Peers meet to combine the shares
</div>

Your trusted peers can proceed to the [Combine Secrets](/combine) page even without your intervention. There they will find 3 text input fiels where to type or paste their share. Up to all of you to decide how this can happen, if the shares should be communicated to one person, or if all must be present and type it in, or dictate by phone, etc.

<div class="well well-sm">
7) Your secret is revealed to your peers
</div>

Once the 3 shares are submitted, our software will show the original secret exactly as you typed it in. If there was a typo in any of the strings our software will return an error, so be careful to check that every single letter of the share is correct: a mistake in communication can make it impossible to retrieve your secret.

## How secure is Secrets

Secret is written in a stateless language, does not make use of any database nor atoms and all content passing through it is not saved, only transformed and shown on screen.  This service is a functional demo allowing anyone to split up to 1Kb of secret data into 5 shares of which 3 (a number we call "quorum") are enough to recover the initial secret. 

Anyone planning to use Secrets for mission critical environments is encouraged to build his/her own version: this is possible on any PC operating system supporting Java. By running Secrets autonomously is also possible to change its configuration, for instance to:
- accept larger secrets
- split into more or less than 5 shares
- change the minimum amount of shares needed to recover the secret
- change the alphabetical subset used to compose the shares
- change the cryptographic "salt", a secret key that is unique to each installation

If the secret being shared is really precious one should also consider adding an additional layer of encryption, for instance by using symmetric password encryption or even using public PGP keys of all participants.

To share large files it is recommended to use filesystem encryption (for instance using our other software [Tomb](https://dyne.org/software/tomb)) with a key that is then shared: this way the encrypted files can be stored in duplicate copies in possession of every participant, but they will be accessed only when enough participants agree.

## Building Secrets on your own computer


<img class="pull-right" src="https://secrets.dyne.org/static/img/clojure.png">

Secrets is written in Clojure and is fully cross-platform: one can run it locally on a GNU/Linux machine, as well on Apple/OSX and MS/Windows.

<img class="pull-left" src="https://secrets.dyne.org/static/img/leiningen.jpg"
	style="padding-right: 1.5em">

The following software is required: [OpenJDK](http://openjdk.java.net), [Clojure](http://clojure.org), [Leiningen](http://leiningen.org).

For instance on Devuan systems one can install all necessary dependencies using apt and the following packages: `apt-get openjdk-7-jdk libversioneer-clojure haveged`.

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

This command will open a browser pointing on the service running locally on http://localhost:3000

To start only a web server for the application, but no browser, run: `server-headless`

### Configure

The configuration file used by Secrets is found in its own directory (the one where its started from) and called `config.json`, an example holding defaults is `example-config.json`. To run Secrets in production is at least necessary to change the `salt` configuration item with a random generated string which has to remain constant for the installation and should not be made public. The salt of an installation is important for all those who use that particular instance, since it is a cryptographic element necessary to retrieve all secrets generated by that installation.

Other configuration items are self explanatory and can be left untouched.

## Acknowledgments

<img src="https://secrets.dyne.org/static/img/haarlemsche_abc.jpg"
	alt="Haarlem's Alphabet textile, from a Vlisco exhibition"
	title="Haarlem's Alphabet textile, from a Vlisco exhibition"
	style="float: right; width: 300px">

Secrets is Free and Open Source research and development activity funded by the European Commission in the context of the [Collective Awareness Platforms for Sustainability and Social Innovation (CAPSSI)](https://ec.europa.eu/digital-single-market/en/collective-awareness) program. Secrets uses the underlying [FXC](https://github.com/dyne/FXC) cryptographic protocol, whose use cases relate to trust management and social digital currency, explained in detail in the deliverable [Implementation of digital social currency infrastructure (D5.5)](http://dcentproject.eu/wp-content/uploads/2015/10/D5.5-Implementation-of-digital-social-currency-infrastructure-.pdf) produced as part of the [D-CENT project](http://dcentproject.eu) (grant nr. 610349) and adopted as a component of the social wallet toolkit being developed for the [PIE project](https://github.com/pienews) (grant nr. 687922).

Industry standard addressed: Information technology -- Security techniques -- Secret sharing
- Part 1: General - [ISO/IEC 19592-1:2016](https://www.iso.org/standard/65422.html)
- Part 2: Fundamental mechanisms - [ISO/IEC FDIS 19592-2 (Under development)](https://www.iso.org/standard/65425.html)

The [Secret Sharing](https://en.wikipedia.org/wiki/Secret_sharing) algorithm adopted is based on [Shamir's Secret Sharing](https://en.wikipedia.org/wiki/Shamir%27s_Secret_Sharing), references:
- Shamir, Adi (1979), "How to share a secret", Communications of the ACM 22 (11): 612–613
- Knuth, D. E. (1997), The Art of Computer Programming, II: Seminumerical Algorithms: 505

The Secret Sharing algorithm used internally is implemented in Java by Tim Tiemens, adopted with a 4096 cipher prime number. The Integer Compression algorithm used internally is the FastPFOR128 by Daniel Lemire, see: Lemire, D. and Boytsov, L. "[Decoding billions of integers per second through vectorization](http://arxiv.org/abs/1209.2137)" (2015).

## License

Secrets is Copyright (C) 2015-2017 by the Dyne.org Foundation

Secrets software and documentation are designed, written and maintained by Denis Roio <jaromil@dyne.org>

Thanks for ideas and inspirations to Francesca Bria, Marco Sachy and Enric Durán Giralt.

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
