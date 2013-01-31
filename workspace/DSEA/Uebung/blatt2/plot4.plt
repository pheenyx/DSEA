# Plotten von Aufgabe 2.3c
#===========================
set title 'Aufgabe 2.3c'
#set yrange [-30000:0]
#set xrange [0:1e6]
#unset logscale xy
set logscale xy
set pointsize 0.5
set grid
set xlabel 'x'
set ylabel 'f(x)'

f(x)=a*x**2+b*x*+c
fit f(x) 'C:\Users\PhoenX\Dropbox\Uni\DSEA\workspace\DSEA\Uebung\blatt2\plot4.dat' via a, b, c
plot 'C:\Users\PhoenX\Dropbox\Uni\DSEA\workspace\DSEA\Uebung\blatt2\plot.dat' with points , f(x)