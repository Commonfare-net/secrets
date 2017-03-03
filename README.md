# FXC - Freecoin's Secret Sharing

[![software by Dyne.org](https://www.dyne.org/wp-content/uploads/2015/12/software_by_dyne.png)](http://www.dyne.org)

FXC is the underlying cryptographic protocol used in [Freecoin](https://github.com/pienews/freecoin). It is used to generate passwords that are split into pieces to be distributed to friends, so that when the password is lost it can be recuperated by putting together the pieces.

The FXC protocol and its use cases are explained in detail in this document [Implementation of digital social currency infrastructure (D5.5)](http://dcentproject.eu/wp-content/uploads/2015/10/D5.5-Implementation-of-digital-social-currency-infrastructure-.pdf) produced as part of the research conducted in the [D-CENT project](http://dcentproject.eu/resource_category/publications/).


## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen



## Running

To start a web server for the application, run:

    lein ring server



## License

Part of Decentralized Citizen Engagement Technologies (D-CENT)

R&D funded by the European Commission (FP7/CAPS 610349)

Designed and maintained by Denis Roio <jaromil@dyne.org>

Based on the Shamir Secret Sharing algorithm by Adi Shamir
 - Shamir, Adi (1979), "How to share a secret", Communications of the ACM 22 (11): 612–613
 - Knuth, D. E. (1997), The Art of Computer Programming, II: Seminumerical Algorithms: 505

Implemented using the Secret Share Java library by Tim Tiemens with a 4096 cyphers prime number.

Industry standard references:
 - [ISO/IEC 19592-1:2016](https://www.iso.org/standard/65422.html) Information technology -- Security techniques -- Secret sharing -- Part 1: General
 - [ISO/IEC FDIS 19592-2 (Under development)](https://www.iso.org/standard/65425.html)  Information technology -- Security techniques -- Secret sharing -- Part 2: Fundamental mechanisms


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
