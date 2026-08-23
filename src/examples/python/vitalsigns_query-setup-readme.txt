How to run Vitalsigns Publishing Performance Test Tool on a new computer

1. Download & install Java 17 SDK - https://download.oracle.com/java/17/archive/jdk-17.0.12_windows-x64_bin.exe
2. Install Portico 2.1.1 (portico-2.1.1-win64.exe)
3. Launch Python 3.13.7 installer (python-3.13.7-amd64.exe). Select checkbox to add 'python' executable to path, then install
4. Copy this folder (devpackage/src/examples/python) to Downloads/python
5. Unzip devpackage_4.0.0.b12.zip to Downloads
6. Launch Powershell instance #1
7. cd Downloads\devpackage_4.0.0.b12\devpackage
8. .\run.bat to launch JETS Developer Package
9. Launch Powershell instance #2
10. cd Downloads\python
11. rmdir .\__pycache__\
12. rmdir venv
13. python -m venv venv
13. Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
14. Enter "A" (Yes to All)
15. .\venv\Scripts\Activate.ps1
16. pip install requests
17. python .\vitalsigns_query.py --patient krishen --rps 1000 --duration 60