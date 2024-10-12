# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-roadsign.git;protocol=ssh;branch=main \
           file://scull_makefile.patch \
           file://scull \ 
           file://scull_load.sh \
           file://scull_unload.sh \
           "

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "283b7a507414b748b8f5f99921163ed1ab88cc4a"

S = "${WORKDIR}/git"

inherit module

EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"
EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/scull"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.

RPROVIDES:${PN} += "kernel-module-scull"

# Installation of startup scripts
inherit update-rc.d

FILES:${PN} += "${sysconfdir}/init.d/scull \ 
                ${sysconfdir}/init.d/scull_load.sh \
                ${sysconfdir}/init.d/scull_unload.sh \
                "

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "scull"
#INITSCRIPT_PARAMS:${PN} = "defaults"

# Use do_install:append to install the script after the module is installed
do_install:append () {
    
    # Install the autostart scripts
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/scull ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/scull_load.sh ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/scull_unload.sh ${D}${sysconfdir}/init.d

}


