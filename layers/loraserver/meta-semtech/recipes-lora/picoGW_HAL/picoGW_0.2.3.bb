DESCRIPTION = "Driver/HAL to build a gateway using a concentrator board based on Semtech SX1301 and a USB MCU"
HOMEPAGE = "https://github.com/Lora-net/picoGW_hal"
PRIORITY = "optional"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a2bdef95625509f821ba00460e3ae0eb"
PR = "r6"

SRC_URI = "git://github.com/Lora-net/picoGW_hal.git;protocol=git \
           file://library.cfg \
"
SRCREV = "a955619271b5d0a46d32e08150acfbc1eed183b7"

S = "${WORKDIR}/git"
CFLAGS += "-Iinc -I."

do_configure_append() {
    cp ${WORKDIR}/library.cfg ${S}/libpicoGWHAL/library.cfg
}

do_compile() {
    oe_runmake
}

do_install() {
    install -d ${D}${libdir}/lora
    install -d ${D}${includedir}/lora

    install -m 0644 libpicoGWHAL/libloragw.a ${D}${libdir}/lora
    install -m 0644 libpicoGWHAL/library.cfg ${D}${libdir}/lora
    install -m 0644 libpicoGWHAL/inc/* ${D}${includedir}/lora

    install -d ${D}/opt/lora-gateway/gateway-utils
    install -m 0755 libpicoGWHAL/test_* ${D}/opt/lora-gateway/gateway-utils/
    install -m 0755 util_pkt_logger/util_pkt_logger ${D}/opt/lora-gateway/gateway-utils/
    install -m 0755 util_boot/util_spectral_scan ${D}/opt/lora-gateway/gateway-utils/
    install -m 0755 util_com_stress/util_spi_stress ${D}/opt/lora-gateway/gateway-utils/
    install -m 0755 util_tx_test/util_tx_test ${D}/opt/lora-gateway/gateway-utils/
    install -m 0755 util_tx_continuous/util_tx_continuous ${D}/opt/lora-gateway/gateway-utils/
    install -m 0755 util_chip_id/util_lbt_test ${D}/opt/lora-gateway/gateway-utils/
}

PACKAGES += "${PN}-utils ${PN}-utils-dbg"

FILES_${PN}-staticdev = "${libdir}/lora"
FILES_${PN}-utils = "/opt/lora-gateway/gateway-utils/*"
FILES_${PN}-utils-dbg = "/opt/lora-gateway/gateway-utils/.debug"
FILES_${PN}-dev = "${includedir}/lora ${libdir}/lora/library.cfg"

INSANE_SKIP_${PN}-utils = "ldflags"
