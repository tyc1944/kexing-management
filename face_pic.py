#coding=utf-8
import os
import sys
import shutil


def each_file(filePath,new_filepath):
    l_dir=os.listdir(filePath)
    for one_dir in l_dir:
        fullpath = os.path.join('%s\%s' % (filePath,one_dir))
        new_fullpath = os.path.join('%s\%s' % (new_filepath,one_dir))
        if os.path.isfile(fullpath):
            shutil.copy(fullpath,new_fullpath)
        else:
            each_file(fullpath,new_filepath)

if __name__ == '__main__':
    old_path = "E:/project_files/kexing/dir_pic"
    new_path = "E:/project_files/kexing/no_dir_pic"
    each_file(old_path,new_path)