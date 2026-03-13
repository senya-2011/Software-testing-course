package com.tpo.labs.lab2

import kotlin.math.PI

object TestTables {
    const val tableLookupEps = 1e-9

    val symmetryPoints = listOf(PI / 6.0, PI / 4.0, PI / 3.0)
    val periodicPoints = listOf(-PI / 3.0, -PI / 6.0, PI / 6.0, PI / 3.0)

    val sinControlValues = linkedMapOf(
        -PI / 3.0 to -0.8660254037844386,
        -PI / 4.0 to -0.7071067811865476,
        -PI / 6.0 to -0.5,
        0.0 to 0.0,
        PI / 6.0 to 0.5,
        PI / 4.0 to 0.7071067811865476,
        PI / 3.0 to 0.8660254037844386,
        PI / 2.0 to 1.0
    )

    val cosControlValues = linkedMapOf(
        -PI / 3.0 to 0.5,
        -PI / 4.0 to 0.7071067811865476,
        -PI / 6.0 to 0.8660254037844386,
        0.0 to 1.0,
        PI / 6.0 to 0.8660254037844386,
        PI / 4.0 to 0.7071067811865476,
        PI / 3.0 to 0.5,
        PI / 2.0 to 0.0
    )

    val secControlValues = linkedMapOf(
        -PI / 3.0 to 2.0,
        -PI / 4.0 to 1.4142135623730951,
        -PI / 6.0 to 1.1547005383792517,
        0.0 to 1.0,
        PI / 6.0 to 1.1547005383792517,
        PI / 4.0 to 1.4142135623730951,
        PI / 3.0 to 2.0
    )

    val cscControlValues = linkedMapOf(
        -PI / 3.0 to -1.1547005383792517,
        -PI / 4.0 to -1.4142135623730951,
        -PI / 6.0 to -2.0,
        PI / 6.0 to 2.0,
        PI / 4.0 to 1.4142135623730951,
        PI / 3.0 to 1.1547005383792517,
        PI / 2.0 to 1.0
    )

    val tanControlValues = linkedMapOf(
        -PI / 3.0 to -1.7320508075688772,
        -PI / 4.0 to -1.0,
        -PI / 6.0 to -0.5773502691896257,
        0.0 to 0.0,
        PI / 6.0 to 0.5773502691896257,
        PI / 4.0 to 1.0,
        PI / 3.0 to 1.7320508075688772
    )

    val cotControlValues = linkedMapOf(
        -PI / 3.0 to -0.5773502691896257,
        -PI / 4.0 to -1.0,
        -PI / 6.0 to -1.7320508075688772,
        PI / 6.0 to 1.7320508075688772,
        PI / 4.0 to 1.0,
        PI / 3.0 to 0.5773502691896257,
        PI / 2.0 to 0.0
    )

    val lnControlValues = linkedMapOf(
        0.25 to -1.3862943611198906,
        0.5 to -0.6931471805599453,
        1.0 to 0.0,
        2.0 to 0.6931471805599453,
        3.0 to 1.0986122886681098,
        5.0 to 1.6094379124341003
    )

    val log2ControlValues = linkedMapOf(
        0.25 to -2.0,
        0.5 to -1.0,
        1.0 to 0.0,
        2.0 to 1.0,
        3.0 to 1.5849625007211563,
        5.0 to 2.321928094887362
    )

    val log3ControlValues = linkedMapOf(
        0.25 to -1.2618595071429148,
        0.5 to -0.6309297535714574,
        1.0 to 0.0,
        2.0 to 0.6309297535714574,
        3.0 to 1.0,
        5.0 to 1.4649735207179269
    )

    val log5ControlValues = linkedMapOf(
        0.25 to -0.8613531161467861,
        0.5 to -0.43067655807339306,
        1.0 to 0.0,
        2.0 to 0.43067655807339306,
        3.0 to 0.6826061944859854,
        5.0 to 1.0
    )

    val trigSystemDomainPoints = listOf(-1.06, -1.05, -1.04)
    val logSystemDomainPoints = listOf(0.5, 2.0, 5.0)

    val sinSystemValues = linkedMapOf(
        -1.06 to -0.8723554823449863,
        -1.05 to -0.867423225594017,
        -1.04 to -0.8624042272433384
    )

    val cosSystemValues = linkedMapOf(
        -1.06 to 0.4888720818605275,
        -1.05 to 0.49757104789172696,
        -1.04 to 0.5062202572327784
    )

    val secSystemValues = linkedMapOf(
        -1.06 to 2.0455248665340937,
        -1.05 to 2.009763237304762,
        -1.04 to 1.9754247004385757
    )

    val cscSystemValues = linkedMapOf(
        -1.06 to -1.1463216776168947,
        -1.05 to -1.1528397793536063,
        -1.04 to -1.1595490471985328
    )

    val tanSystemValues = linkedMapOf(
        -1.06 to -1.784424831594013,
        -1.05 to -1.7433153099831704,
        -1.04 to -1.7036146122591331
    )

    val cotSystemValues = linkedMapOf(
        -1.06 to -0.5604046650184238,
        -1.05 to -0.5736196970642412,
        -1.04 to -0.5869872169468644
    )

    val lnSystemValues = linkedMapOf(
        0.5 to -0.6931471805599453,
        2.0 to 0.6931471805599453,
        5.0 to 1.6094379124341003
    )

    val log2SystemValues = linkedMapOf(
        0.5 to -1.0,
        2.0 to 1.0,
        5.0 to 2.321928094887362
    )

    val log3SystemValues = linkedMapOf(
        0.5 to -0.6309297535714574,
        2.0 to 0.6309297535714574,
        5.0 to 1.4649735207179269
    )

    val log5SystemValues = linkedMapOf(
        0.5 to -0.43067655807339306,
        2.0 to 0.43067655807339306,
        5.0 to 1.0
    )

    val systemTrigValues = linkedMapOf(
        -1.06 to 2.248846318720647,
        -1.05 to 1.879006684430395,
        -1.04 to 2.149517613825098
    )

    val systemLogValues = linkedMapOf(
        0.5 to 172.96986409452387,
        2.0 to 27.26867157343539,
        5.0 to 0.07852673737495784
    )
}
