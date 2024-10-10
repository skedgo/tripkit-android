package com.skedgo.tripkit.mock

class MockServiceResponse {
    companion object {
        const val SERVICE_RESPONSE_TEST = """
            {
                "realTimeStatus": "CAPABLE",
                "type": "bus",
                "shapes": [
                    {
                        "stops": [
                            {
                                "name": "Argyle St near Kent St",
                                "bearing": 80,
                                "departure": 1436773980,
                                "arrival": 1436709599,
                                "lng": 151.2039,
                                "code": "200099",
                                "lat": -33.85847
                            },
                            {
                                "name": "Argyle St near Lower Fort St",
                                "bearing": 51,
                                "departure": 1436774040,
                                "arrival": 1436774040,
                                "lng": 151.20526,
                                "code": "2000100",
                                "lat": -33.85823
                            },
                            {
                                "name": "Lower Fort St near Windmill St",
                                "bearing": 47,
                                "departure": 1436774055,
                                "arrival": 1436774055,
                                "lng": 151.20618,
                                "code": "2000101",
                                "lat": -33.85746
                            },
                            {
                                "name": "Lower Fort St opp Cumberland St",
                                "bearing": 63,
                                "departure": 1436774100,
                                "arrival": 1436774100,
                                "lng": 151.207,
                                "code": "2000102",
                                "lat": -33.85669
                            },
                            {
                                "name": "George St near Lower Fort St",
                                "bearing": 143,
                                "departure": 1436774115,
                                "arrival": 1436774115,
                                "lng": 151.20775,
                                "code": "2000103",
                                "lat": -33.8563
                            },
                            {
                                "name": "George St near Hickson Rd",
                                "bearing": 193,
                                "departure": 1436774160,
                                "arrival": 1436774160,
                                "lng": 151.2092,
                                "code": "2000104",
                                "lat": -33.85828
                            },
                            {
                                "name": "George St opp Globe St",
                                "bearing": 208,
                                "departure": 1436774220,
                                "arrival": 1436774220,
                                "lng": 151.20856,
                                "code": "2000106",
                                "lat": -33.86091
                            },
                            {
                                "name": "George St opp Essex St",
                                "bearing": 185,
                                "departure": 1436774280,
                                "arrival": 1436774280,
                                "lng": 151.20769,
                                "code": "2000107",
                                "lat": -33.86251
                            },
                            {
                                "name": "George St opp Wynyard",
                                "bearing": 184,
                                "departure": 1436774400,
                                "arrival": 1436774400,
                                "lng": 151.20731,
                                "code": "2000175",
                                "lat": -33.86619
                            },
                            {
                                "name": "George St near Strand Arcade",
                                "bearing": 180,
                                "departure": 1436774580,
                                "arrival": 1436774580,
                                "lng": 151.20703,
                                "code": "2000218",
                                "lat": -33.86954
                            },
                            {
                                "name": "George St before Bathurst St",
                                "bearing": 197,
                                "departure": 1436774880,
                                "arrival": 1436774880,
                                "lng": 151.20699,
                                "code": "2000222",
                                "lat": -33.87404
                            },
                            {
                                "name": "George St near Liverpool St",
                                "bearing": 193,
                                "departure": 1436775000,
                                "arrival": 1436775000,
                                "lng": 151.20609,
                                "code": "2000113",
                                "lat": -33.87688
                            },
                            {
                                "name": "George St before Campbell St",
                                "bearing": 197,
                                "departure": 1436775060,
                                "arrival": 1436775060,
                                "lng": 151.20564,
                                "code": "2000174",
                                "lat": -33.87876
                            },
                            {
                                "name": "George St near Barlow St",
                                "bearing": 208,
                                "departure": 1436775180,
                                "arrival": 1436775180,
                                "lng": 151.20502,
                                "code": "2000114",
                                "lat": -33.88071
                            },
                            {
                                "name": "Railway Square, George St - Stand D",
                                "bearing": 261,
                                "departure": 1436775300,
                                "arrival": 1436775300,
                                "lng": 151.20341,
                                "code": "200064",
                                "shortName": "Stand D",
                                "lat": -33.88363
                            },
                            {
                                "name": "Broadway at Buckland St",
                                "bearing": 275,
                                "departure": 1436775540,
                                "arrival": 1436775540,
                                "lng": 151.19763,
                                "code": "200815",
                                "lat": -33.88449
                            },
                            {
                                "name": "Glebe Point Rd near Parramatta Rd",
                                "bearing": 305,
                                "departure": 1436775720,
                                "arrival": 1436775720,
                                "lng": 151.1924,
                                "code": "203729",
                                "lat": -33.8841
                            },
                            {
                                "name": "Glebe Point near Derby Pl",
                                "bearing": 305,
                                "departure": 1436775780,
                                "arrival": 1436775780,
                                "lng": 151.19133,
                                "code": "203730",
                                "lat": -33.88336
                            },
                            {
                                "name": "Glebe Point Rd near Mitchell St",
                                "bearing": 306,
                                "departure": 1436775840,
                                "arrival": 1436775840,
                                "lng": 151.18983,
                                "code": "203731",
                                "lat": -33.88231
                            },
                            {
                                "name": "Glebe Point Rd near St Johns Rd",
                                "bearing": 316,
                                "departure": 1436775960,
                                "arrival": 1436775960,
                                "lng": 151.18805,
                                "code": "203732",
                                "lat": -33.88106
                            },
                            {
                                "name": "Glebe Point Rd near Bridge Rd",
                                "bearing": 321,
                                "departure": 1436776020,
                                "arrival": 1436776020,
                                "lng": 151.18727,
                                "code": "203733",
                                "lat": -33.88027
                            },
                            {
                                "name": "Glebe Point Rd near Hereford St",
                                "bearing": 323,
                                "departure": 1436776080,
                                "arrival": 1436776080,
                                "lng": 151.1859,
                                "code": "203734",
                                "lat": -33.87858
                            },
                            {
                                "name": "Glebe Point Rd near Wigram Rd",
                                "bearing": 323,
                                "departure": 1436776140,
                                "arrival": 1436776140,
                                "lng": 151.18518,
                                "code": "203735",
                                "lat": -33.87764
                            },
                            {
                                "name": "Glebe Point Rd near Toxteth Rd",
                                "bearing": 322,
                                "departure": 1436776200,
                                "arrival": 1436776200,
                                "lng": 151.18373,
                                "code": "203715",
                                "lat": -33.87578
                            },
                            {
                                "name": "Glebe Point Rd near Pendrill St",
                                "bearing": 305,
                                "departure": 1436776260,
                                "arrival": 1436776260,
                                "lng": 151.1821,
                                "code": "203716",
                                "lat": -33.87376
                            },
                            {
                                "name": "Federal Rd Terminus",
                                "arrival": 1436776320,
                                "lng": 151.18001,
                                "code": "203717",
                                "lat": -33.87234
                            }
                        ],
                        "serviceNumber": "431",
                        "travelled": true,
                        "encodedWaypoints": "n~smEk_{y[[jAU{I??Ja@cDsC????{CeD???BmAgB@o@??@@z@yAlIiE???@~DnAjIl@???@tAl@~DfBj@T???@bFVbENnDRfCL??r@DT@lDXbGd@`DQ??@@TBbFEnCCl@@|FFjEB???@bARvFlA|Cn@|A^??x@PzBz@`EH??@@bATlBb@xCp@t@N??@@rAZhCp@|D~@RPdClCNN??@?dApFjAdGDtEDnD@lAHvD??CbB@~BDlI@lBb@nEBp@s@HeAjB??@@wCrE??@@qEfH????{FdJ??@@q@n@mBfB??@?wAfA{FjE??@@a@C{CnC????sJ`H??@?sKfI???@wKnHd@jB`AXRU",
                        "serviceTripID": "75113881_20150609_11954",
                        "operator": "Sydney Buses",
                        "serviceName": "Glebe Point"
                    }
                ]
            }
        """

        const val SERVICE_RESPONSE_TEST_STOPS = """
            {
                "realTimeStatus": "CAPABLE",
                "shapes": [
                    {
                        "encodedWaypoints": "jztmEuviz[hAwJxAB???@jIhA???@bF`AIx@lCF???BzDt@GkA???@h@{F????Ry@nO|B????hGt@???@lI`A???@zKd@??@@tH~@???@lIjAX_H??@@d@gAlBLnC_E????bBs@hHE????hBH|FeE???@tBgBhDmA???@vBAbDfD??@?Tr@z@NlDkAlCz@??@@pAlDcAhC`@lDLfA??@Bs@xD??@@{AhDMjC??@?Y|@JfDV|@HZ`BrA??^p@nCrCvAzA??@@~@nA|BzCp@Ax@~@tCHrCm@|@S??@BxB@bBfAPU???@w@zH??@@c@d@[vA{@dMQpC???@k@bH??cA~L??@?OhB[jD??Ch@m@hHUlC???@c@rG??Gp@w@pLMfB??@@MxCkFbG??wBxBl@`Dr@vAj@lAhAtH??KZoEp@O`G??IxA??@ACj@Y\\jEXMrB??wAjH??aCjLg@jC???@c@xB_@bC?????rCtChN???@?rGwDnO_A`Ce@dBm@dA??{B~BsDfE???ByAvBwEhH??k@f@uAlBu@nBQbE??@?Ip@k@dGGVc@jC??kC~Je@jB??@?}@bCwC`CmApA??y@b@y@~Bo@fB??w@fBwBrEeBxD??@@c@h@wBtCoBdC??@Bi@t@_D|Dg@l@??@@uBvB?p@CjGA`DStAaKI??PDeAc@oGa@cGI??_Gw@s@C_@PkBc@uBEmFM??WYoHc@SA_AmAeDyAuEU??i@AyAQRvCcHU",
                        "operator": "Sydney Buses",
                        "serviceName": "CITY Circular Quay",
                        "serviceNumber": "380",
                        "serviceTripID": "76467191_20151006_11954",
                        "stops": [
                            {
                                "arrival": 1468677599,
                                "bearing": 113,
                                "code": "203026",
                                "departure": 1468763040,
                                "lat": -33.86293,
                                "lng": 151.27931,
                                "name": "Military Rd near Old South Head Rd"
                            },
                            {
                                "arrival": 1468763100,
                                "bearing": 192,
                                "code": "203027",
                                "departure": 1468763100,
                                "lat": -33.86375,
                                "lng": 151.28117,
                                "name": "Military Rd near Kimberley St"
                            },
                            {
                                "arrival": 1468763110,
                                "bearing": 200,
                                "code": "203028",
                                "departure": 1468763110,
                                "lat": -33.86542,
                                "lng": 151.28079,
                                "name": "Military Rd near Eastern Av"
                            },
                            {
                                "arrival": 1468763120,
                                "bearing": 174,
                                "code": "203029",
                                "departure": 1468763120,
                                "lat": -33.86721,
                                "lng": 151.28012,
                                "name": "Peel St near George St"
                            },
                            {
                                "arrival": 1468763160,
                                "bearing": 99,
                                "code": "203030",
                                "departure": 1468763160,
                                "lat": -33.86811,
                                "lng": 151.28021,
                                "name": "Lancaster Rd near Peel St"
                            },
                            {
                                "arrival": 1468763175,
                                "bearing": 186,
                                "code": "203037",
                                "departure": 1468763175,
                                "lat": -33.86832,
                                "lng": 151.28146,
                                "name": "Lancaster Rd near Military Rd"
                            },
                            {
                                "arrival": 1468763220,
                                "bearing": 191,
                                "code": "203038",
                                "departure": 1468763220,
                                "lat": -33.87107,
                                "lng": 151.28113,
                                "name": "Military Rd near Weonga Rd"
                            },
                            {
                                "arrival": 1468763230,
                                "bearing": 191,
                                "code": "203039",
                                "departure": 1468763230,
                                "lat": -33.87239,
                                "lng": 151.28085,
                                "name": "Military Rd near Blake St"
                            },
                            {
                                "arrival": 1468763240,
                                "bearing": 185,
                                "code": "203040",
                                "departure": 1468763240,
                                "lat": -33.87406,
                                "lng": 151.28052,
                                "name": "Military Rd near Dover Rd"
                            },
                            {
                                "arrival": 1468763280,
                                "bearing": 192,
                                "code": "203041",
                                "departure": 1468763280,
                                "lat": -33.87612,
                                "lng": 151.28032,
                                "name": "Military Rd near Liverpool St"
                            },
                            {
                                "arrival": 1468763295,
                                "bearing": 149,
                                "code": "203042",
                                "departure": 1468763295,
                                "lat": -33.87768,
                                "lng": 151.27998,
                                "name": "Military Rd near Raleigh St"
                            },
                            {
                                "arrival": 1468763340,
                                "bearing": 139,
                                "code": "203049",
                                "departure": 1468763340,
                                "lat": -33.87948,
                                "lng": 151.28104,
                                "name": "Military Rd near Arthur St"
                            },
                            {
                                "arrival": 1468763400,
                                "bearing": 171,
                                "code": "203050",
                                "departure": 1468763400,
                                "lat": -33.88095,
                                "lng": 151.28227,
                                "name": "Military Rd near Wentworth St"
                            },
                            {
                                "arrival": 1468763415,
                                "bearing": 152,
                                "code": "203051",
                                "departure": 1468763415,
                                "lat": -33.88295,
                                "lng": 151.28256,
                                "name": "Military Rd near Hugh Bamford Reserve"
                            },
                            {
                                "arrival": 1468763460,
                                "bearing": 147,
                                "code": "202631",
                                "departure": 1468763460,
                                "lat": -33.88475,
                                "lng": 151.28351,
                                "name": "Military Rd opp O'Donnell St"
                            },
                            {
                                "arrival": 1468763470,
                                "bearing": 210,
                                "code": "202632",
                                "departure": 1468763470,
                                "lat": -33.88618,
                                "lng": 151.28441,
                                "name": "Military Rd opp Blair St"
                            },
                            {
                                "arrival": 1468763480,
                                "bearing": 187,
                                "code": "202633",
                                "departure": 1468763480,
                                "lat": -33.88761,
                                "lng": 151.28357,
                                "name": "Military Rd opp Wallis Pde"
                            },
                            {
                                "arrival": 1468763520,
                                "bearing": 263,
                                "code": "202634",
                                "departure": 1468763520,
                                "lat": -33.88961,
                                "lng": 151.28331,
                                "name": "Campbell Pde Terminus"
                            },
                            {
                                "arrival": 1468763580,
                                "bearing": 286,
                                "code": "202635",
                                "departure": 1468763580,
                                "lat": -33.88993,
                                "lng": 151.2805,
                                "name": "Campbell Pde near Ramsgate Av"
                            },
                            {
                                "arrival": 1468763595,
                                "bearing": 289,
                                "code": "202636",
                                "departure": 1468763595,
                                "lat": -33.88967,
                                "lng": 151.27956,
                                "name": "Campbell Pde near Queen Elizabeth Dr"
                            },
                            {
                                "arrival": 1468763640,
                                "bearing": 253,
                                "code": "202637",
                                "departure": 1468763640,
                                "lat": -33.88916,
                                "lng": 151.278,
                                "name": "Campbell Pde opp Wairoa Av"
                            },
                            {
                                "arrival": 1468763700,
                                "bearing": 227,
                                "code": "202638",
                                "departure": 1468763700,
                                "lat": -33.88975,
                                "lng": 151.27597,
                                "name": "Campbell Pde opp Curlewis St"
                            },
                            {
                                "arrival": 1468763760,
                                "bearing": 200,
                                "code": "202652",
                                "departure": 1468763760,
                                "lat": -33.89107,
                                "lng": 151.27452,
                                "name": "Campbell Pde opp Hall St"
                            },
                            {
                                "arrival": 1468763820,
                                "bearing": 192,
                                "code": "202654",
                                "departure": 1468763820,
                                "lat": -33.89437,
                                "lng": 151.2733,
                                "name": "Campbell Pde near Notts Av"
                            },
                            {
                                "arrival": 1468763835,
                                "bearing": 280,
                                "code": "202655",
                                "departure": 1468763835,
                                "lat": -33.89558,
                                "lng": 151.27303,
                                "name": "Bondi Rd near Sandridge St"
                            },
                            {
                                "arrival": 1468763880,
                                "bearing": 281,
                                "code": "202656",
                                "departure": 1468763880,
                                "lat": -33.89531,
                                "lng": 151.27144,
                                "name": "Bondi Rd near Dudley St"
                            },
                            {
                                "arrival": 1468763940,
                                "bearing": 279,
                                "code": "202659",
                                "departure": 1468763940,
                                "lat": -33.89461,
                                "lng": 151.26779,
                                "name": "Bondi Rd near Denham St"
                            },
                            {
                                "arrival": 1468763955,
                                "bearing": 279,
                                "code": "202660",
                                "departure": 1468763955,
                                "lat": -33.89439,
                                "lng": 151.26633,
                                "name": "Bondi Rd near Imperial Av"
                            },
                            {
                                "arrival": 1468764000,
                                "bearing": 279,
                                "code": "202661",
                                "departure": 1468677600,
                                "lat": -33.89404,
                                "lng": 151.26408,
                                "name": "Bondi Rd near Watson St"
                            },
                            {
                                "arrival": 1468764015,
                                "bearing": 279,
                                "code": "202662",
                                "departure": 1468677615,
                                "lat": -33.89383,
                                "lng": 151.2627,
                                "name": "Bondi Rd near Ocean St"
                            },
                            {
                                "arrival": 1468764060,
                                "bearing": 278,
                                "code": "202663",
                                "departure": 1468677660,
                                "lat": -33.89347,
                                "lng": 151.26028,
                                "name": "Bondi Rd near Park Pde"
                            },
                            {
                                "arrival": 1468764075,
                                "bearing": 278,
                                "code": "202254",
                                "departure": 1468677675,
                                "lat": -33.8933,
                                "lng": 151.2589,
                                "name": "Bondi Rd opp Flood St"
                            },
                            {
                                "arrival": 1468764120,
                                "bearing": 301,
                                "code": "202255",
                                "departure": 1468677720,
                                "lat": -33.8929,
                                "lng": 151.25595,
                                "name": "Bondi Rd near Council St"
                            },
                            {
                                "arrival": 1468764135,
                                "bearing": 262,
                                "code": "202256",
                                "departure": 1468677735,
                                "lat": -33.89167,
                                "lng": 151.25388,
                                "name": "Bondi Rd near Waverley Cr"
                            },
                            {
                                "arrival": 1468764180,
                                "bearing": 305,
                                "code": "202257",
                                "departure": 1468677780,
                                "lat": -33.89214,
                                "lng": 151.25008,
                                "name": "Oxford St near Bronte Rd"
                            },
                            {
                                "arrival": 1468764240,
                                "bearing": 278,
                                "code": "202268",
                                "departure": 1468677840,
                                "lat": -33.89097,
                                "lng": 151.2484,
                                "name": "Bondi Junction Interchange (Set Down)"
                            },
                            {
                                "arrival": 1468764360,
                                "bearing": 232,
                                "code": "202281",
                                "departure": 1468677960,
                                "lat": -33.89091,
                                "lng": 151.24794,
                                "name": "Bondi Junction Interchange - Stand N",
                                "shortName": "Stand N"
                            },
                            {
                                "arrival": 1468764420,
                                "bearing": 287,
                                "code": "202258",
                                "departure": 1468678020,
                                "lat": -33.89172,
                                "lng": 151.24687,
                                "name": "Oxford St near Newland St"
                            },
                            {
                                "arrival": 1468764480,
                                "bearing": 287,
                                "code": "202259",
                                "departure": 1468678080,
                                "lat": -33.89129,
                                "lng": 151.24538,
                                "name": "Oxford St near Denison St"
                            },
                            {
                                "arrival": 1468764540,
                                "bearing": 285,
                                "code": "202260",
                                "departure": 1468678140,
                                "lat": -33.89043,
                                "lng": 151.24254,
                                "name": "Oxford St near Waverley Bus Depot"
                            },
                            {
                                "arrival": 1468764555,
                                "bearing": 256,
                                "code": "202147",
                                "departure": 1468678155,
                                "lat": -33.89009,
                                "lng": 151.24126,
                                "name": "Oxford St near Loch Av"
                            },
                            {
                                "arrival": 1468764600,
                                "bearing": 287,
                                "code": "202148",
                                "departure": 1468678200,
                                "lat": -33.89084,
                                "lng": 151.23807,
                                "name": "Oxford St near Moncur St"
                            },
                            {
                                "arrival": 1468764660,
                                "bearing": 313,
                                "code": "202150",
                                "departure": 1468678260,
                                "lat": -33.88918,
                                "lng": 151.23253,
                                "name": "Oxford St near Queen St"
                            },
                            {
                                "arrival": 1468764720,
                                "bearing": 307,
                                "code": "202151",
                                "departure": 1468678320,
                                "lat": -33.88767,
                                "lng": 151.23088,
                                "name": "Oxford St near Paddington Primary"
                            },
                            {
                                "arrival": 1468764735,
                                "bearing": 294,
                                "code": "202152",
                                "departure": 1468678335,
                                "lat": -33.88614,
                                "lng": 151.22878,
                                "name": "Oxford St opp William St"
                            },
                            {
                                "arrival": 1468764780,
                                "bearing": 282,
                                "code": "202153",
                                "departure": 1468678380,
                                "lat": -33.88512,
                                "lng": 151.22649,
                                "name": "Oxford St near Oatley Rd"
                            },
                            {
                                "arrival": 1468764840,
                                "bearing": 290,
                                "code": "202154",
                                "departure": 1468678440,
                                "lat": -33.88465,
                                "lng": 151.22411,
                                "name": "Oxford St opp Brodie St"
                            },
                            {
                                "arrival": 1468764855,
                                "bearing": 310,
                                "code": "202155",
                                "departure": 1468678455,
                                "lat": -33.88375,
                                "lng": 151.22165,
                                "name": "Oxford St opp Hopewell St"
                            },
                            {
                                "arrival": 1468764900,
                                "bearing": 302,
                                "code": "202196",
                                "departure": 1468678500,
                                "lat": -33.88231,
                                "lng": 151.21992,
                                "name": "Oxford St near Verona St"
                            },
                            {
                                "arrival": 1468764915,
                                "bearing": 300,
                                "code": "201060",
                                "departure": 1468678515,
                                "lat": -33.88148,
                                "lng": 151.21858,
                                "name": "Oxford St near Victoria St"
                            },
                            {
                                "arrival": 1468764960,
                                "bearing": 309,
                                "code": "201051",
                                "departure": 1468678560,
                                "lat": -33.88009,
                                "lng": 151.21608,
                                "name": "Oxford St opp Palmer St"
                            },
                            {
                                "arrival": 1468765020,
                                "bearing": 310,
                                "code": "201053",
                                "departure": 1468678620,
                                "lat": -33.87877,
                                "lng": 151.21443,
                                "name": "Oxford St near Riley St"
                            },
                            {
                                "arrival": 1468765080,
                                "bearing": 308,
                                "code": "201056",
                                "departure": 1468678680,
                                "lat": -33.87757,
                                "lng": 151.21297,
                                "name": "Oxford St near Brisbane St"
                            },
                            {
                                "arrival": 1468765200,
                                "bearing": 8,
                                "code": "200055",
                                "departure": 1468678800,
                                "lat": -33.87493,
                                "lng": 151.20958,
                                "name": "Museum Station, Elizabeth St, Stand D",
                                "shortName": "Stand D"
                            },
                            {
                                "arrival": 1468765320,
                                "bearing": 7,
                                "code": "200057",
                                "departure": 1468678920,
                                "lat": -33.872,
                                "lng": 151.20995,
                                "name": "Sheraton on the Park, Elizabeth St"
                            },
                            {
                                "arrival": 1468765380,
                                "bearing": 18,
                                "code": "200082",
                                "departure": 1468678980,
                                "lat": -33.86798,
                                "lng": 151.21043,
                                "name": "Martin Place Station, Elizabeth St, Stand D",
                                "shortName": "Stand D"
                            },
                            {
                                "arrival": 1468765500,
                                "bearing": 345,
                                "code": "200059",
                                "departure": 1468679100,
                                "lat": -33.86403,
                                "lng": 151.2117,
                                "name": "Museum of Sydney, Phillip St"
                            },
                            {
                                "arrival": 1468765560,
                                "code": "200065",
                                "lat": -33.862,
                                "lng": 151.21115,
                                "name": "Circular Quay, Young St, Stand D",
                                "shortName": "Stand D"
                            }
                        ],
                        "travelled": true,
                        "wheelchairAccessible": true
                    }
                ],
                "type": "bus"
            }
        """

        const val SERVICE_RESPONSE_TEST_REAL_TIME = """
            {
                "realTimeStatus": "IS_REAL_TIME",
                "realtimeVehicle": {
                    "id": "125571819",
                    "lastUpdate": 1468594846,
                    "location": {
                        "bearing": 6,
                        "lat": -33.86224,
                        "lng": 151.21207
                    }
                },
                "realtimeAlternativeVehicle": [
                    {
                        "id": "125571819",
                        "lastUpdate": 1468594846,
                        "location": {
                            "bearing": 6,
                            "lat": -33.86224,
                            "lng": 151.21207
                        }
                    }
                ],
                "shapes": [
                    {
                        "encodedWaypoints": "nrtmE}n|y[Sr@v@BbIb@]aDhBAXB??|Er@dDzA~@hAR@nFH??F@f@HxGXnHr@|Lf@??GCfA`@tGb@vLf@hFR??E?rAHEcBh@}DbFa@b@_B??@@hAcB^yF??@@VgBt@cFr@G??@@~Fn@tD^??@@j@FzHz@fAJ??@BpI~@????jI`AbCZjCZ??nF~@ZRb@XjCJ??@?fIv@??xO`A??a@r@f@?Eo@h@sE\\[fDjChDpC??@@j@h@|ItInFdF????vEfGp@uBEiA??@DvAeHrC|@??@?nIzD??@B~TfK????vBnAlA[l@mI~A{D|C\\bDtA????nPlEN}B????f@iB`JdB??@@rLbDlGU??@@lHJ^eI???@tA{NhCJ??@BdABa@fC~@TF|B????uAzO??@DWd@tNzA??@@t@Pv@Rh@uJ??@BhAyM???@P[xAf@d@qJ???@TiAjJjF???@`F~C??@@Xd@]bJ??@DgAtM??BDcBfU??@@{@zL???@G|@o@~H???@sArQ",
                        "operator": "Sydney Buses",
                        "serviceName": "Mascot",
                        "serviceNumber": "301",
                        "serviceTripID": "125571819_20151006_11954",
                        "stops": [
                            {
                                "arrival": 1468591199,
                                "bearing": 171,
                                "code": "2000147",
                                "departure": 1468595700,
                                "lat": -33.86168,
                                "lng": 151.21152,
                                "name": "Circular Quay, Alfred St, Stand A",
                                "shortName": "Stand A"
                            },
                            {
                                "arrival": 1468682160,
                                "bearing": 197,
                                "code": "200068",
                                "departure": 1468595760,
                                "lat": -33.86398,
                                "lng": 151.21185,
                                "name": "Phillip St opp Museum of Sydney"
                            },
                            {
                                "arrival": 1468682280,
                                "bearing": 186,
                                "code": "200081",
                                "departure": 1468595880,
                                "lat": -33.86755,
                                "lng": 151.21071,
                                "name": "Martin Place Station, Elizabeth St, Stand F",
                                "shortName": "Stand F"
                            },
                            {
                                "arrival": 1468682460,
                                "bearing": 187,
                                "code": "2000167",
                                "departure": 1468596060,
                                "lat": -33.87294,
                                "lng": 151.21005,
                                "name": "Hyde Park, Elizabeth St, Stand A",
                                "shortName": "Stand A"
                            },
                            {
                                "arrival": 1468682640,
                                "bearing": 132,
                                "code": "200074",
                                "departure": 1468596240,
                                "lat": -33.87803,
                                "lng": 151.20943,
                                "name": "Museum Station, Downing Centre, Stand B",
                                "shortName": "Stand B"
                            },
                            {
                                "arrival": 1468682760,
                                "bearing": 107,
                                "code": "201026",
                                "departure": 1468596360,
                                "lat": -33.87992,
                                "lng": 151.21147,
                                "name": "Hunt St near Campbell St"
                            },
                            {
                                "arrival": 1468682820,
                                "bearing": 111,
                                "code": "201027",
                                "departure": 1468596420,
                                "lat": -33.88046,
                                "lng": 151.21321,
                                "name": "Campbell St near Riley St"
                            },
                            {
                                "arrival": 1468682880,
                                "bearing": 190,
                                "code": "201095",
                                "departure": 1468596480,
                                "lat": -33.88111,
                                "lng": 151.2149,
                                "name": "Crown St near Campbell St"
                            },
                            {
                                "arrival": 1468682940,
                                "bearing": 190,
                                "code": "201094",
                                "departure": 1468596540,
                                "lat": -33.88332,
                                "lng": 151.21449,
                                "name": "Crown St near Albion St"
                            },
                            {
                                "arrival": 1468683000,
                                "bearing": 191,
                                "code": "201093",
                                "departure": 1468596600,
                                "lat": -33.88549,
                                "lng": 151.21408,
                                "name": "Crown St near Foveaux St"
                            },
                            {
                                "arrival": 1468683015,
                                "bearing": 191,
                                "code": "201092",
                                "departure": 1468596615,
                                "lat": -33.88718,
                                "lng": 151.21375,
                                "name": "Crown St near Rainford St"
                            },
                            {
                                "arrival": 1468683120,
                                "bearing": 195,
                                "code": "201098",
                                "departure": 1468596720,
                                "lat": -33.89021,
                                "lng": 151.21313,
                                "name": "Crown St before Cleveland St"
                            },
                            {
                                "arrival": 1468683180,
                                "bearing": 189,
                                "code": "201642",
                                "departure": 1468596780,
                                "lat": -33.89243,
                                "lng": 151.21252,
                                "name": "Baptist St near Cleveland St"
                            },
                            {
                                "arrival": 1468683195,
                                "bearing": 187,
                                "code": "201645",
                                "departure": 1468596795,
                                "lat": -33.89408,
                                "lng": 151.21225,
                                "name": "Baptist St at Telopea La"
                            },
                            {
                                "arrival": 1468683240,
                                "bearing": 186,
                                "code": "201641",
                                "departure": 1468596840,
                                "lat": -33.89677,
                                "lng": 151.21191,
                                "name": "Phillip St near Baptist St"
                            },
                            {
                                "arrival": 1468683360,
                                "bearing": 224,
                                "code": "201742",
                                "departure": 1468596960,
                                "lat": -33.89882,
                                "lng": 151.21167,
                                "name": "Bourke St near Danks St"
                            },
                            {
                                "arrival": 1468683420,
                                "bearing": 195,
                                "code": "201775",
                                "departure": 1468597020,
                                "lat": -33.90199,
                                "lng": 151.20859,
                                "name": "Bourke St opp Powell St"
                            },
                            {
                                "arrival": 1468683480,
                                "bearing": 136,
                                "code": "201734",
                                "departure": 1468597080,
                                "lat": -33.9033,
                                "lng": 151.20822,
                                "name": "O'Dea Av near Bourke St"
                            },
                            {
                                "arrival": 1468683495,
                                "bearing": 208,
                                "code": "201744",
                                "departure": 1468597095,
                                "lat": -33.90449,
                                "lng": 151.20935,
                                "name": "Joynton Av near Wolseley Gr"
                            },
                            {
                                "arrival": 1468683540,
                                "bearing": 209,
                                "code": "201745",
                                "departure": 1468597140,
                                "lat": -33.90618,
                                "lng": 151.20842,
                                "name": "Joynton Av near Gadigal Av"
                            },
                            {
                                "arrival": 1468683600,
                                "bearing": 151,
                                "code": "201731",
                                "departure": 1468597200,
                                "lat": -33.9097,
                                "lng": 151.20644,
                                "name": "Joynton Av opp Old South Sydney Hospital"
                            },
                            {
                                "arrival": 1468683660,
                                "bearing": 187,
                                "code": "201887",
                                "departure": 1468597260,
                                "lat": -33.91301,
                                "lng": 151.20821,
                                "name": "Rosebery Av near Crewe Pl"
                            },
                            {
                                "arrival": 1468683720,
                                "bearing": 179,
                                "code": "201850",
                                "departure": 1468597320,
                                "lat": -33.91589,
                                "lng": 151.20781,
                                "name": "Kimberley Gr near Dalmeny Av"
                            },
                            {
                                "arrival": 1468683735,
                                "bearing": 191,
                                "code": "201890",
                                "departure": 1468597335,
                                "lat": -33.91786,
                                "lng": 151.20782,
                                "name": "Dalmeny Av near Rippon Way"
                            },
                            {
                                "arrival": 1468683780,
                                "bearing": 137,
                                "code": "201854",
                                "departure": 1468597380,
                                "lat": -33.92141,
                                "lng": 151.20711,
                                "name": "Dalmeny Av near Harcourt Pde"
                            },
                            {
                                "arrival": 1468683840,
                                "bearing": 114,
                                "code": "201845",
                                "departure": 1468597440,
                                "lat": -33.92308,
                                "lng": 151.20866,
                                "name": "Gardeners Rd 150m E of Maloney St"
                            },
                            {
                                "arrival": 1468683900,
                                "bearing": 249,
                                "code": "201847",
                                "departure": 1468597500,
                                "lat": -33.92421,
                                "lng": 151.21114,
                                "name": "Racecourse Pl near Gardeners Rd"
                            },
                            {
                                "arrival": 1468683960,
                                "bearing": 280,
                                "code": "201848",
                                "departure": 1468597560,
                                "lat": -33.92476,
                                "lng": 151.20967,
                                "name": "Evans Av near Longworth Av"
                            },
                            {
                                "arrival": 1468683975,
                                "bearing": 195,
                                "code": "201849",
                                "departure": 1468597575,
                                "lat": -33.92433,
                                "lng": 151.20697,
                                "name": "Evans Av near Maloney St"
                            },
                            {
                                "arrival": 1468684020,
                                "bearing": 114,
                                "code": "201856",
                                "departure": 1468597620,
                                "lat": -33.92672,
                                "lng": 151.2063,
                                "name": "Maloney St near George St"
                            },
                            {
                                "arrival": 1468684080,
                                "bearing": 99,
                                "code": "201859",
                                "departure": 1468597680,
                                "lat": -33.92749,
                                "lng": 151.20796,
                                "name": "George St near Maloney St"
                            },
                            {
                                "arrival": 1468684095,
                                "bearing": 112,
                                "code": "201860",
                                "departure": 1468597695,
                                "lat": -33.92787,
                                "lng": 151.21031,
                                "name": "George St near St Helena Pde"
                            },
                            {
                                "arrival": 1468684140,
                                "bearing": 203,
                                "code": "201861",
                                "departure": 1468597740,
                                "lat": -33.92861,
                                "lng": 151.2121,
                                "name": "Florence Av near Lismore St"
                            },
                            {
                                "arrival": 1468684200,
                                "bearing": 215,
                                "code": "201864",
                                "departure": 1468597800,
                                "lat": -33.93053,
                                "lng": 151.21127,
                                "name": "Florence Av near O'Connor St"
                            },
                            {
                                "arrival": 1468684210,
                                "bearing": 271,
                                "code": "201865",
                                "departure": 1468597810,
                                "lat": -33.93166,
                                "lng": 151.21046,
                                "name": "Florence St near King St"
                            },
                            {
                                "arrival": 1468684220,
                                "bearing": 279,
                                "code": "201866",
                                "departure": 1468597820,
                                "lat": -33.93165,
                                "lng": 151.20848,
                                "name": "King St near St Helena Pde"
                            },
                            {
                                "arrival": 1468684260,
                                "bearing": 278,
                                "code": "201867",
                                "departure": 1468597860,
                                "lat": -33.93131,
                                "lng": 151.2061,
                                "name": "King St after Meridian St"
                            },
                            {
                                "arrival": 1468684320,
                                "bearing": 278,
                                "code": "202039",
                                "departure": 1468597920,
                                "lat": -33.93083,
                                "lng": 151.20251,
                                "name": "King St near Hicks Av"
                            },
                            {
                                "arrival": 1468684335,
                                "bearing": 279,
                                "code": "202040",
                                "departure": 1468597935,
                                "lat": -33.93053,
                                "lng": 151.20029,
                                "name": "King St near Sutherland St"
                            },
                            {
                                "arrival": 1468684380,
                                "bearing": 278,
                                "code": "202041",
                                "departure": 1468597980,
                                "lat": -33.93026,
                                "lng": 151.19836,
                                "name": "King St near Frogmore St"
                            },
                            {
                                "arrival": 1468684440,
                                "code": "202042",
                                "lat": -33.92984,
                                "lng": 151.19537,
                                "name": "King St before Botany Rd"
                            }
                        ],
                        "travelled": true,
                        "wheelchairAccessible": true
                    }
                ],
                "type": "bus"
            }
        """
    }

}