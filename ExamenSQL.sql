select * from eventos_politicos;

-- Ver los primeros 10 registros 
SELECT * FROM eventos_politicos LIMIT 10;

-- Contar el total de filas 
SELECT COUNT(*) AS total_eventos FROM eventos_politicos;

-- Suma total de asistentes 
SELECT SUM(asistentes) AS gran_total_asistentes FROM eventos_politicos;

-- Top 5 eventos con más gente
SELECT candidato, evento, asistentes 
FROM eventos_politicos 
ORDER BY asistentes DESC 
LIMIT 5;

-- Ver solo eventos activos 
SELECT * FROM eventos_politicos WHERE activa = 1;

-- Ver eventos cancelados o inactivos 
SELECT * FROM eventos_politicos WHERE activa = 0;

-- eventos de cada partido
SELECT partido, COUNT(*) as cantidad_eventos 
FROM eventos_politicos 
GROUP BY partido;

-- promedio de asistencia por ubicación
SELECT ubicacion, AVG(asistentes) as promedio_gente
FROM eventos_politicos
GROUP BY ubicacion
ORDER BY promedio_gente DESC;