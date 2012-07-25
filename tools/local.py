#-*- coding: utf-8 -*-
""" Local Multi Session Start Script

You have to unpack the distribution file
"blockout-client-1.0.0-SNAPSHOT-dist.zip" into a subfolder called 'orig',
and run this script in the same folder that contains said subfolder.

It will create several copies and run them all in parallel and then collect
the log data into a single subfolder 'logs'.

When you want to terminate all Instances just press Enter.

"""
import os
import subprocess
import shutil as sh
import glob

num_instances = 5
orig_path = './orig'
path_pattern = './%s'
log_dir = './logs/client'
chord_log_dir = './logs/chord'
jar_file = "blockout-client-1.0.0-SNAPSHOT.jar"

make_path = lambda x: path_pattern % (x,)
make_client_log_path = lambda x: '%s/%s.log' % (log_dir, x)
make_chord_log_path = lambda x: '%s/%s.log' % (chord_log_dir, x)

if not os.path.isdir(orig_path):
    print "Source Folder 'orig' missing"
    exit()

if not os.path.isdir(log_dir):
    print "Creating Log Directory"
    os.makedirs(log_dir)

if not os.path.isdir(chord_log_dir):
    print "Creating Chord Log Directory"
    os.makedirs(chord_log_dir)


print "Removing old Instance Folders"
for dir_num in xrange(num_instances):
    path = make_path(dir_num)
    if os.path.isdir(path):
        sh.rmtree(path)

print "Setting up new Instance Folders"
for dir_num in xrange(num_instances):
    path = make_path(dir_num)
    sh.copytree(orig_path, path)

processes = []
print "Starting %s Instances" % num_instances
for dir_num in xrange(num_instances):
    path = make_path(dir_num)
    processes.append(
        subprocess.Popen(
        ["java",
         "-Djava.library.path=natives/",
         "-jar", jar_file,
         "--headless",
         "-p", "auto_instance_%s" % dir_num
     ], cwd=path)
    )
    if any(proc.poll() for proc in processes):
        print "Instance %s did not start correctly. Aborting." % dir_num
        exit()

print "All Instances Started. "
print "Press Enter to Terminate and Collect Logs"
raw_input()

print "Terminating Instances"
for proc in processes:
    proc.terminate()

print "Collecting Logs"
for dir_num in xrange(num_instances):
    path = make_path(dir_num)
    client_log_path = '%s/log/output.log' % path
    sh.copy(client_log_path, make_client_log_path(dir_num))
    chord_log = glob.glob(path + '/*.log')[0]
    sh.copy(chord_log, make_chord_log_path(dir_num))

print "Done"
