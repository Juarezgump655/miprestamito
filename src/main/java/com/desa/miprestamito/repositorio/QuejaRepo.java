package com.desa.miprestamito.repositorio;

import com.desa.miprestamito.Projections.*;
import com.desa.miprestamito.modelo.Queja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuejaRepo extends CrudRepository<Queja, Long> {

    public  Iterable<Queja> findByUsuariocreo(String id);

    public List<Queja> findByIdTipoQueja(Long id);


    @Query(value = " SELECT q.correlativo, pa.nombre_punto_atencion as puntoAtencion, r.nombre as region,\n" +
            "       e.nombre_estado_solicitud as estado, m.nombre_medio as medioIngreso,\n" +
            "       q.fecha_hora_ingreso as fechaCreacion, q.detalle_queja as detalle,\n" +
            " DATE_PART('hour', q.fecha_final - q.fecha_hora_ingreso) AS tiempoOperacion\n" +
            "FROM public.queja q\n" +
            "INNER JOIN puntos_atencion pa ON pa.id_punto_atencion = q.id_punto_atencion\n" +
            "INNER JOIN region r ON r.id_region = pa.id_region\n" +
            "INNER JOIN estados_socitud e ON e.id_estadosolicitud = q.id_estado\n" +
            "INNER JOIN medio_ingreso_queja m ON m.id_medio_ingreso_queja = q.id_medio_ingreso_queja\n" +
            "WHERE q.id_punto_atencion = :id \n" +
            "GROUP BY q.correlativo, pa.nombre_punto_atencion, r.nombre, e.nombre_estado_solicitud, m.nombre_medio, q.fecha_hora_ingreso, q.detalle_queja, q.fecha_final;\n;\n" , nativeQuery = true)
    public Iterable<TableReportesProjection> findByIdPuntoAtencion(@Param("id")  Long id);




    public List<Queja> findByIdEstado(Long id);

    @Query(value = "SELECT q.correlativo as correlativo FROM public.queja q WHERE q.id_queja = :correlativo", nativeQuery = true)
    public CorrelativoProjection findByCorrelativ(Long correlativo);


    @Query(value = "select q.id_queja as idQueja , q.correlativo as correlativo, q.fechacreacion as fechaCreacion from public.queja q where q.id_estado =1 and q.id_punto_asignado is null",
    nativeQuery = true)
    public List<tablaAsignacionQuejaProjection> tablaAsignacionQueja();

    @Query(value = "SELECT q.correlativo, pa.nombre_punto_atencion as puntoAtencion, r.nombre as region,\n" +
            "             e.nombre_estado_solicitud as estado, m.nombre_medio as medioIngreso,\n" +
            "             q.fecha_hora_ingreso as fechaCreacion, q.detalle_queja as detalle,\n" +
            "     DATE_PART('hour', q.fecha_final - q.fecha_hora_ingreso) AS tiempoOperacion\n" +
            "            FROM public.queja q\n" +
            "            INNER JOIN puntos_atencion pa ON pa.id_punto_atencion = q.id_punto_atencion\n" +
            "            INNER JOIN region r ON r.id_region = pa.id_region\n" +
            "            INNER JOIN estados_socitud e ON e.id_estadosolicitud = q.id_estado\n" +
            "            INNER JOIN medio_ingreso_queja m ON m.id_medio_ingreso_queja = q.id_medio_ingreso_queja\n" +
            "\t\t\tWHERE q.fecha_hora_ingreso >= TO_DATE(:fechaInicio,'yyyy/MM/dd') AND q.fecha_hora_ingreso <= TO_DATE(:fechaFin,'yyyy/MM/dd')\n" +
            "\t\t\t GROUP BY q.correlativo, pa.nombre_punto_atencion, r.nombre, e.nombre_estado_solicitud, m.nombre_medio, q.fecha_hora_ingreso, q.detalle_queja, q.fecha_final;\n", nativeQuery = true)
    public List<TableReportesProjection> findByFechas(@Param("fechaInicio") String fechaInicio, @Param("fechaFin") String fechaFin);



    @Query(value="SELECT q.correlativo, pa.nombre_punto_atencion as puntoAtencion, r.nombre as region,\n" +
            "             e.nombre_estado_solicitud as estado, m.nombre_medio as medioIngreso,\n" +
            "             q.fecha_hora_ingreso as fechaCreacion, q.detalle_queja as detalle,\n" +
            "       DATE_PART('hour', q.fecha_final - q.fecha_hora_ingreso) AS tiempoOperacion \n" +
            "            FROM public.queja q\n" +
            "            INNER JOIN puntos_atencion pa ON pa.id_punto_atencion = q.id_punto_atencion\n" +
            "            INNER JOIN region r ON r.id_region = pa.id_region\n" +
            "            INNER JOIN estados_socitud e ON e.id_estadosolicitud = q.id_estado\n" +
            "            INNER JOIN medio_ingreso_queja m ON m.id_medio_ingreso_queja = q.id_medio_ingreso_queja\n" +
            "\t\t\tWHERE q.correlativo  =  :correlativo\n" +
            "\t\t\tGROUP BY q.correlativo, pa.nombre_punto_atencion, r.nombre, e.nombre_estado_solicitud, m.nombre_medio, q.fecha_hora_ingreso, q.detalle_queja, q.fecha_final;\n", nativeQuery = true)
    public List<TableReportesProjection> findByCorrelativo(@Param("correlativo") String correlativo);

    @Query(value = "\t\tSELECT q.correlativo, pa.nombre_punto_atencion as puntoAtencion, r.nombre as region,\n" +
            "             e.nombre_estado_solicitud as estado, m.nombre_medio as medioIngreso,\n" +
            "             q.fecha_hora_ingreso as fechaCreacion, q.detalle_queja as detalle,\n" +
            "     DATE_PART('hour', q.fecha_final - q.fecha_hora_ingreso) AS tiempoOperacion\n" +
            "            FROM public.queja q\n" +
            "            INNER JOIN puntos_atencion pa ON pa.id_punto_atencion = q.id_punto_atencion\n" +
            "            INNER JOIN region r ON r.id_region = pa.id_region\n" +
            "            INNER JOIN estados_socitud e ON e.id_estadosolicitud = q.id_estado\n" +
            "            INNER JOIN medio_ingreso_queja m ON m.id_medio_ingreso_queja = q.id_medio_ingreso_queja\n" +
            "\t\t\tWHERE q.id_punto_atencion  =  :puntoAtencion\n" +
            "\t\t\tGROUP BY q.correlativo, pa.nombre_punto_atencion, r.nombre, e.nombre_estado_solicitud, m.nombre_medio, q.fecha_hora_ingreso, q.detalle_queja, q.fecha_final;\n" , nativeQuery = true)
    public List<TableReportesProjection> findByPuntoAtencion(@Param("puntoAtencion") Long puntoAtencion);


    @Query(value = "\tSELECT q.correlativo, pa.nombre_punto_atencion as puntoAtencion, r.nombre as region,\n" +
            "             e.nombre_estado_solicitud as estado, m.nombre_medio as medioIngreso,\n" +
            "             q.fecha_hora_ingreso as fechaCreacion, q.detalle_queja as detalle," +
            "      DATE_PART('hour', q.fecha_final - q.fecha_hora_ingreso) AS tiempoOperacion\n" +
            "            FROM public.queja q\n" +
            "            INNER JOIN puntos_atencion pa ON pa.id_punto_atencion = q.id_punto_atencion\n" +
            "            INNER JOIN region r ON r.id_region = pa.id_region\n" +
            "            INNER JOIN estados_socitud e ON e.id_estadosolicitud = q.id_estado\n" +
            "            INNER JOIN medio_ingreso_queja m ON m.id_medio_ingreso_queja = q.id_medio_ingreso_queja\n" +
            "\t\t\tWHERE r.id_region  =  :region\n" +
            "\t\t\tGROUP BY q.correlativo, pa.nombre_punto_atencion, r.nombre, e.nombre_estado_solicitud, m.nombre_medio, q.fecha_hora_ingreso, q.detalle_queja, q.fecha_final;\n", nativeQuery = true)
    public  List<TableReportesProjection> findByRegion(@Param("region") Long region);

    @Query(value = "select  q.correlativo  as correlativo, \n" +

            "\t\tmiq.nombre_medio as nombreMedio,\n" +
            "\t\tq.fecha_hora_ingreso as fechaIngreso,\n" +
            "\t\ttq.descripcion_queja as descripcionTipoQueja,\n" +
            "\t\tq.detalle_queja as detalleQueja\n" +
            "from public.queja q \n" +
            "inner join public.medio_ingreso_queja miq on\n" +
            "q.id_medio_ingreso_queja = miq.id_medio_ingreso_queja \n" +
            "inner join public.tipo_quejas tq on\n" +
            "q.id_tipo_queja = tq.id_tipo_queja\n" +
            "where q.id_queja =?1",nativeQuery = true)
    public fichaQuejaProjection fichaQueja(Long idQueja);

    @Query(value = "SELECT q.id_queja as idQueja, q.correlativo, pa.nombre_punto_atencion as puntoAtencion, r.nombre as region,\n" +
            "             e.nombre_estado_solicitud as estado, m.nombre_medio as medioIngreso,\n" +
            "             q.fecha_hora_ingreso as fechaCreacion, q.detalle_queja as detalle, \n" +
            "             u.nombre\n" +
            "            FROM public.queja q\n" +
            "            INNER JOIN puntos_atencion pa ON pa.id_punto_atencion = q.id_punto_atencion\n" +
            "            INNER JOIN region r ON r.id_region = pa.id_region\n" +
            "            INNER JOIN estados_socitud e ON e.id_estadosolicitud = q.id_estado\n" +
            "            INNER JOIN medio_ingreso_queja m ON m.id_medio_ingreso_queja = q.id_medio_ingreso_queja\n" +
            "            inner join usuarios u on u.dpi \t=q.usuariocreo \n" +
            "\t\t\tWHERE q.id_punto_atencion  =  :idPuntoAtencion\n" +
            "\t\t\tand q.id_estado = 2\n" +
            "\t\t\tor q.id_estado =7\n" +
            "\t\t\tGROUP BY q.correlativo, q.id_queja, pa.nombre_punto_atencion, r.nombre, e.nombre_estado_solicitud, m.nombre_medio, q.fecha_hora_ingreso, q.detalle_queja,\n" +
            "\t\t\t\t\t\tu.nombre;\n", nativeQuery = true)
    public List<TableQuejaSeguimientoProjection> findByPuntoAtencionAtendidas(@Param("idPuntoAtencion") Long idPuntoAtencion);


}
