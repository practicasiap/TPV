-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 25-01-2019 a las 09:50:04
-- Versión del servidor: 10.1.29-MariaDB
-- Versión de PHP: 7.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `tpv`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `albaran`
--

CREATE TABLE `albaran` (
  `id_albaran` int(11) NOT NULL,
  `cod_albaran` varchar(100) NOT NULL,
  `cod_proveedor` varchar(100) NOT NULL,
  `fecha` varchar(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `albaran`
--

INSERT INTO `albaran` (`id_albaran`, `cod_albaran`, `cod_proveedor`, `fecha`) VALUES
(24, '75574', '34645756767', '2019-01-24'),
(22, '657', '465467', '2019-01-24'),
(21, '45', '56', '2019-01-24'),
(25, '89765', '6543222765', '2019-01-24'),
(27, '8976543', '756432', '2019-01-24'),
(28, '675435678', '897654322', '2019-01-24'),
(26, '897654', '897654356789', '2019-01-24'),
(23, '345645', '9806874563', '2019-01-24');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asig_rol`
--

CREATE TABLE `asig_rol` (
  `cod` int(20) NOT NULL,
  `cod_rol` int(20) NOT NULL,
  `cod_emple` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `caja`
--

CREATE TABLE `caja` (
  `cod` int(20) NOT NULL,
  `descripcion` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `codigo` int(20) NOT NULL,
  `nombre` int(20) NOT NULL,
  `descripcion` int(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id` int(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido1` varchar(50) NOT NULL,
  `apellido2` varchar(50) NOT NULL,
  `dni` varchar(9) NOT NULL,
  `direccion` varchar(100) NOT NULL,
  `telefono` int(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id`, `nombre`, `apellido1`, `apellido2`, `dni`, `direccion`, `telefono`) VALUES
(1, 'Sergio', 'Mansilla', 'Insti', '09876545u', 'Calle de Mansilla, nº6, 4ºA', 669944330),
(2, 'Victor', 'Nieva', 'Insti', '05599335f', 'Calle de Nieva, nº88, Escalera B, 1ºA', 992244004);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `desglose_albaran`
--

CREATE TABLE `desglose_albaran` (
  `id_desglose_albaran` int(11) NOT NULL,
  `cod_albaran` varchar(100) NOT NULL,
  `cod_producto` varchar(100) NOT NULL,
  `cod_producto_proveedor` varchar(100) DEFAULT NULL,
  `unidades` int(11) DEFAULT NULL,
  `precio_unidad` float DEFAULT NULL,
  `fecha_caducidad` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `desglose_albaran`
--

INSERT INTO `desglose_albaran` (`id_desglose_albaran`, `cod_albaran`, `cod_producto`, `cod_producto_proveedor`, `unidades`, `precio_unidad`, `fecha_caducidad`) VALUES
(15, '21', '1', '345', 2, 20, '2019-01-22'),
(16, '22', '1', 'uyiu', 2, 20, '2019-01-31'),
(17, '23', '1', '34534', 2, 10, '2019-01-29'),
(18, '24', '1', '4534', 6, 30, '2019-01-29'),
(19, '25', '1', '67543', 2, 20, '2019-01-28'),
(20, '26', '1', '2345678', 6, 30, '2019-01-29'),
(21, '27', '1', '2345678', 3, 30, '2019-01-30'),
(22, '28', '1', '56432567', 20, 30, '2019-01-29');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleados`
--

CREATE TABLE `empleados` (
  `cod` int(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido1` varchar(50) NOT NULL,
  `apellido2` varchar(50) NOT NULL,
  `s_social` varchar(20) NOT NULL,
  `dni` varchar(9) NOT NULL,
  `direccion` varchar(100) NOT NULL,
  `telefono` int(20) NOT NULL,
  `f_nacimiento` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `empleados`
--

INSERT INTO `empleados` (`cod`, `nombre`, `apellido1`, `apellido2`, `s_social`, `dni`, `direccion`, `telefono`, `f_nacimiento`) VALUES
(301, 'Antonio', 'Pérez', 'Medina', '928473618 9', '05938148j', 'Calle Morería, nº5', 668844330, '1988-01-21'),
(512, 'Laura', 'Vera', 'Molina', '928173618 6', '09876543j', 'Calle Numancia, nº13, 3ºA', 665544779, '1999-04-07'),
(935, 'Luis Fernando', 'Valtorre', 'Sánchez', '83618394 9', '05929473k', 'Calle Montoro, nº44, Escalera 2, 4ºC', 669922110, '1965-01-26');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empresa`
--

CREATE TABLE `empresa` (
  `cif` varchar(20) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `direccion` varchar(20) NOT NULL,
  `localidad` varchar(20) NOT NULL,
  `telefono` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventario`
--

CREATE TABLE `inventario` (
  `id_inventario` int(11) NOT NULL,
  `fecha` varchar(12) DEFAULT NULL,
  `cod_producto` varchar(100) DEFAULT NULL,
  `precio_unidad` float(11,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `iva`
--

CREATE TABLE `iva` (
  `cod` int(20) NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `porcentaje` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `codigo` int(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `cantidad` int(20) NOT NULL,
  `precio_compra` float NOT NULL,
  `precio_venta` float NOT NULL,
  `cod_barras` int(30) NOT NULL,
  `desc_breve` varchar(50) NOT NULL,
  `desc_larga` varchar(500) DEFAULT NULL,
  `cod_pro_proveedor` int(20) DEFAULT NULL,
  `subcategoria` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`codigo`, `nombre`, `cantidad`, `precio_compra`, `precio_venta`, `cod_barras`, `desc_breve`, `desc_larga`, `cod_pro_proveedor`, `subcategoria`) VALUES
(1, 'Danone', 30, 29.3333, 1, 333333, 'algo', NULL, 56432567, 1),
(3, 'Danone', 24, 0.366154, 0.44, 1, 'algo', NULL, NULL, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedores`
--

CREATE TABLE `proveedores` (
  `id_proveedor` int(11) NOT NULL,
  `CIF` varchar(100) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `telefono` varchar(100) DEFAULT NULL,
  `pais` varchar(100) DEFAULT NULL,
  `provincia` varchar(100) DEFAULT NULL,
  `ciudad` varchar(100) DEFAULT NULL,
  `cp` varchar(6) DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `contacto` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `cod` int(20) NOT NULL,
  `nombre` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `subcategorias`
--

CREATE TABLE `subcategorias` (
  `codigo` int(20) NOT NULL,
  `cod_categoria` int(20) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `descripcion` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ticket`
--

CREATE TABLE `ticket` (
  `cod` int(20) NOT NULL,
  `fecha` varchar(20) NOT NULL,
  `hora` varchar(20) NOT NULL,
  `num_caja` int(20) NOT NULL,
  `cod_emple` int(20) NOT NULL,
  `cod_cliente` int(20) NOT NULL,
  `total` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `turno`
--

CREATE TABLE `turno` (
  `id_turno` int(11) NOT NULL,
  `cod_empleado` varchar(100) NOT NULL,
  `fecha` varchar(12) NOT NULL,
  `hora_entrada` varchar(10) NOT NULL,
  `hora_salida` varchar(10) DEFAULT NULL,
  `efectivo_entrada` float(11,2) DEFAULT NULL,
  `efectivo_salida` float(11,2) DEFAULT NULL,
  `cod_caja` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventas`
--

CREATE TABLE `ventas` (
  `id_ventas` int(11) NOT NULL,
  `cod_ticket` varchar(100) NOT NULL,
  `cod_producto` varchar(100) NOT NULL,
  `unidades` int(11) DEFAULT NULL,
  `precio_unidad` float(11,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `albaran`
--
ALTER TABLE `albaran`
  ADD PRIMARY KEY (`cod_proveedor`,`cod_albaran`),
  ADD UNIQUE KEY `id_albaran` (`id_albaran`);

--
-- Indices de la tabla `asig_rol`
--
ALTER TABLE `asig_rol`
  ADD PRIMARY KEY (`cod`);

--
-- Indices de la tabla `caja`
--
ALTER TABLE `caja`
  ADD PRIMARY KEY (`cod`);

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `desglose_albaran`
--
ALTER TABLE `desglose_albaran`
  ADD PRIMARY KEY (`cod_albaran`,`cod_producto`,`fecha_caducidad`) USING BTREE,
  ADD UNIQUE KEY `id_desglose_albaran` (`id_desglose_albaran`);

--
-- Indices de la tabla `empleados`
--
ALTER TABLE `empleados`
  ADD PRIMARY KEY (`cod`);

--
-- Indices de la tabla `empresa`
--
ALTER TABLE `empresa`
  ADD PRIMARY KEY (`cif`);

--
-- Indices de la tabla `inventario`
--
ALTER TABLE `inventario`
  ADD PRIMARY KEY (`id_inventario`);

--
-- Indices de la tabla `iva`
--
ALTER TABLE `iva`
  ADD PRIMARY KEY (`cod`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`codigo`),
  ADD UNIQUE KEY `cod_barras` (`cod_barras`);

--
-- Indices de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  ADD PRIMARY KEY (`CIF`),
  ADD UNIQUE KEY `codProveedor` (`id_proveedor`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`cod`);

--
-- Indices de la tabla `subcategorias`
--
ALTER TABLE `subcategorias`
  ADD PRIMARY KEY (`codigo`);

--
-- Indices de la tabla `ticket`
--
ALTER TABLE `ticket`
  ADD PRIMARY KEY (`cod`);

--
-- Indices de la tabla `turno`
--
ALTER TABLE `turno`
  ADD PRIMARY KEY (`cod_empleado`,`fecha`,`hora_entrada`),
  ADD UNIQUE KEY `id_turno` (`id_turno`);

--
-- Indices de la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD PRIMARY KEY (`cod_ticket`,`cod_producto`),
  ADD UNIQUE KEY `id_ventas` (`id_ventas`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `albaran`
--
ALTER TABLE `albaran`
  MODIFY `id_albaran` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT de la tabla `asig_rol`
--
ALTER TABLE `asig_rol`
  MODIFY `cod` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `desglose_albaran`
--
ALTER TABLE `desglose_albaran`
  MODIFY `id_desglose_albaran` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT de la tabla `empleados`
--
ALTER TABLE `empleados`
  MODIFY `cod` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=936;

--
-- AUTO_INCREMENT de la tabla `inventario`
--
ALTER TABLE `inventario`
  MODIFY `id_inventario` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `iva`
--
ALTER TABLE `iva`
  MODIFY `cod` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `codigo` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  MODIFY `id_proveedor` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `rol`
--
ALTER TABLE `rol`
  MODIFY `cod` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `subcategorias`
--
ALTER TABLE `subcategorias`
  MODIFY `codigo` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `ticket`
--
ALTER TABLE `ticket`
  MODIFY `cod` int(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `turno`
--
ALTER TABLE `turno`
  MODIFY `id_turno` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `ventas`
--
ALTER TABLE `ventas`
  MODIFY `id_ventas` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
