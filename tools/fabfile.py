from fabric.api import *


env.user = "darmstadtple_p2plab"


def all_nodes():
    env.hosts = [
        "pl2.zju.edu.cn",
        "plab3.cs.msu.ru",
        "planetlab1.iitkgp.ac.in",
        "planetlab2.iitkgp.ac.in",
        "planetlab1.postel.org",
        "planetlab4.postel.org",
        "pl2.cis.uab.edu",
        "pllx1.parc.xerox.com",
        "pllx2.parc.xerox.com",
        "plab1.cs.msu.ru",
        "planetlab-5.ece.iastate.edu",
        "planetlab-01.ece.uprm.edu",
        "planetlab-2.cse.ohio-state.edu",
        "planetlab3.netmedia.gist.ac.kr",
        "planetlab2.inf.ethz.ch",
        "planetlab-02.bu.edu",
        "planetlab2.utdallas.edu",
        "planetlab1.iis.sinica.edu.tw",
        "plnodea.plaust.edu.cn",
        "planetlab5.csee.usf.edu",
        "plgmu4.ite.gmu.edu",
        "planetlab1.tsuniv.edu",
        "planetlab1.cti.espol.edu.ec",
        "planetlab2.cti.espol.edu.ec",
        "planetlab1.temple.edu",
        "planetlab2.temple.edu",
    ]


def all_but_supernode():
    env.hosts = [
        "pl2.zju.edu.cn",
        "plab3.cs.msu.ru",
        "planetlab1.iitkgp.ac.in",
        "planetlab2.iitkgp.ac.in",
        "planetlab1.postel.org",
        "planetlab4.postel.org",
        "pl2.cis.uab.edu",
        "pllx2.parc.xerox.com",
        "plab1.cs.msu.ru",
        "planetlab-5.ece.iastate.edu",
        "planetlab-01.ece.uprm.edu",
        "planetlab-2.cse.ohio-state.edu",
        "planetlab3.netmedia.gist.ac.kr",
        "planetlab2.inf.ethz.ch",
        "planetlab-02.bu.edu",
        "planetlab2.utdallas.edu",
        "planetlab1.iis.sinica.edu.tw",
        "plnodea.plaust.edu.cn",
        "planetlab5.csee.usf.edu",
        "plgmu4.ite.gmu.edu",
        "planetlab1.tsuniv.edu",
        "planetlab1.cti.espol.edu.ec",
        "planetlab2.cti.espol.edu.ec",
        "planetlab1.temple.edu",
        "planetlab2.temple.edu",
    ]


def supernode():
    env.hosts = ["pllx1.parc.xerox.com"]


def test():
    run("pkill")


def setup_environment():
    run("mkdir blockout")


def upload_game():
    with cd("blockout"):
        run("wget --quiet http://www.inside-php.de/blockout.tar.bz2")


def clean_up():
    run("rm -rf blockout")


def unpack_game():
    with cd("blockout"):
        run("tar xfj blockout.tar.bz2")


def show_upload_state():
    with cd("blockout"):
        run("ls -lah blockout.tar.bz2")


def show_unpack_state():
    run("du -s blockout")


def start_game(address, port):
    with cd("blockout"):
        run("../jre/bin/java -Djava.library.path=natives/ -jar blockout-client-1.0.0-SNAPSHOT.jar --headless -p someplayer --bootstrap %s --bootstrapPort %s &" % (address, port))


def start_supernode():
    with cd("blockout"):
        run("../jre/bin/java -Djava.library.path=natives/ -jar blockout-client-1.0.0-SNAPSHOT.jar --headless -p someplayer &")


def get_ports():
    with cd("blockout"):
        run("grep 'Bound server to /' log/output.log ")


def stop_game():
    sudo("pkill -o -f blockout-client")


def ps_aux():
    run("ps aux | grep blockout-client")


def delete_logs():
    with cd("blockout"):
        run("rm -f log/output.log")
        run("rm -f *.log")


def get_logs():
    with cd("blockout"):
        name = run("ls *.log")
        result = run("cat *.log")
        with open("logs/%s" % name, 'w') as f:
            f.write(result)


def update_loglevel():
    with cd("blockout/conf"):
        run("rm logback.xml")
        run("wget --quiet http://www.inside-php.de/logback.xml")
