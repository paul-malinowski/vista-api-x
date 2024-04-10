
/*

drop table authStationX
drop table authPermissionX
drop table authAppX

*/


SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[authAppX](
	[id] [bigint] IDENTITY(1000000,1) NOT NULL,
	[app] [varchar](150) NOT NULL,
	[key] [varchar](150) NOT NULL,
	[allowVistaToken] [tinyint] NOt NULL,
	[comments] [varchar](255) NULL,
	[oplock] [bigint] NOT NULL,
	[active] [tinyint] NOT NULL,
	[createdAt] [datetime] NOT NULL,
	[modifiedAt] [datetime] NOT NULL,
 CONSTRAINT [pk__auth_appx] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[authAppX] ADD  CONSTRAINT [DF_authAppX_oplock]  DEFAULT ((0)) FOR [oplock]
GO

ALTER TABLE [dbo].[authAppX] ADD  CONSTRAINT [DF_authAppX_active]  DEFAULT ((1)) FOR [active]
GO

ALTER TABLE [dbo].[authAppX] ADD  CONSTRAINT [DF_allowVistaToken]  DEFAULT ((0)) FOR [allowVistaToken]

GO
ALTER TABLE [dbo].[authAppX] ADD  CONSTRAINT [DF_authAppX_createdAt]  DEFAULT (getdate()) FOR [createdAt]
GO

ALTER TABLE [dbo].[authAppX] ADD  CONSTRAINT [DF_authAppX_modifiedAt]  DEFAULT (getdate()) FOR [modifiedAt]
GO

ALTER TABLE [authAppX] ADD UNIQUE ([key]);
GO

CREATE TABLE [dbo].[authPermissionX](
	[id] [bigint] IDENTITY(1000000,1) NOT NULL,
	[appId] [bigint] NOT NULL,
	[context] [varchar](255) NOT NULL,
	[rpc] [varchar](255) NOT NULL,
	[oplock] [bigint] NOT NULL,
	[active] [tinyint] NOT NULL,
	[createdAt] [datetime] NOT NULL,
	[modifiedAt] [datetime] NOT NULL,
 CONSTRAINT [pk__authPermissionX] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[authPermissionX] ADD  CONSTRAINT [DF_authPermissionX_oplock]  DEFAULT ((0)) FOR [oplock]
GO

ALTER TABLE [dbo].[authPermissionX] ADD  CONSTRAINT [DF_authPermissionX_active]  DEFAULT ((1)) FOR [active]
GO

ALTER TABLE [dbo].[authPermissionX] ADD  CONSTRAINT [DF_authPermissionX_createdAt]  DEFAULT (getdate()) FOR [createdAt]
GO

ALTER TABLE [dbo].[authPermissionX] ADD  CONSTRAINT [DF_authPermissionX_modifiedAt]  DEFAULT (getdate()) FOR [modifiedAt]
GO

ALTER TABLE [authPermissionX] ADD UNIQUE (appId, context, rpc);
GO

ALTER TABLE authPermissionX ADD FOREIGN KEY (appId) REFERENCES authAppX(id);
GO


CREATE TABLE [dbo].[authStationX](
	[id] [bigint] IDENTITY(1000000,1) NOT NULL,
	[appId] [bigint] NOT NULL,
	[station] [varchar](255) NOT NULL,
	[duz] [varchar](50) NOT NULL,
	[oplock] [bigint] NOT NULL,
	[active] [tinyint] NOT NULL,
	[createdAt] [datetime] NOT NULL,
	[modifiedAt] [datetime] NOT NULL,
 CONSTRAINT [pk__authStationX] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[authStationX] ADD  CONSTRAINT [DF_authStationX_oplock]  DEFAULT ((0)) FOR [oplock]
GO

ALTER TABLE [dbo].[authStationX] ADD  CONSTRAINT [DF_authStationX_active]  DEFAULT ((1)) FOR [active]
GO

ALTER TABLE [dbo].[authStationX] ADD  CONSTRAINT [DF_authStationX_createdAt]  DEFAULT (getdate()) FOR [createdAt]
GO

ALTER TABLE [dbo].[authStationX] ADD  CONSTRAINT [DF_authStationX_modifiedAt]  DEFAULT (getdate()) FOR [modifiedAt]
GO

ALTER TABLE [authStationX] ADD UNIQUE (appId, station);
GO

ALTER TABLE authStationX ADD FOREIGN KEY (appId) REFERENCES authAppX(id)
GO


USE [VistaApiStaging]
GO

/****** Object:  Table [dbo].[authAppConfigXRef]    Script Date: 12/02/2022 12:56:21 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[authAppConfigXRef](
	[id] [bigint] IDENTITY(1000000,1) NOT NULL,
	[config] [varchar](100) NOT NULL,
	[description] [varchar](255) NULL,
	[oplock] [bigint] NOT NULL,
	[active] [tinyint] NOT NULL,
	[createdAt] [datetime] NOT NULL,
	[modifiedAt] [datetime] NOT NULL,
 CONSTRAINT [__pk_appconfigref] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[authAppConfigXRef] ADD  CONSTRAINT [__appconfigref_oplock]  DEFAULT ((0)) FOR [oplock]
GO

ALTER TABLE [dbo].[authAppConfigXRef] ADD  CONSTRAINT [__appconfigref_active]  DEFAULT ((1)) FOR [active]
GO

ALTER TABLE [dbo].[authAppConfigXRef] ADD  CONSTRAINT [__appconfigref_createdAt]  DEFAULT (getdate()) FOR [createdAt]
GO

ALTER TABLE [dbo].[authAppConfigXRef] ADD  CONSTRAINT [__appconfigref_modifiedAt]  DEFAULT (getdate()) FOR [modifiedAt]
GO

ALTER TABLE  [dbo].[authAppConfigXRef] ADD  CONSTRAINT [__uq_appconfigref_config]  UNIQUE ([config])
GO

CREATE TABLE [dbo].[authAppConfigX](
	[id] [bigint] IDENTITY(1000000,1) NOT NULL,
	[appId] [bigint] NOT NULL,
	[configId] [bigint] NOT NULL,
	[oplock] [bigint] NOT NULL,
	[active] [tinyint] NOT NULL,
	[createdAt] [datetime] NOT NULL,
	[modifiedAt] [datetime] NOT NULL,
 CONSTRAINT [__pk_appconfig] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[authAppConfigX] ADD  CONSTRAINT [__appconfig_oplock]  DEFAULT ((0)) FOR [oplock]
GO

ALTER TABLE [dbo].[authAppConfigX] ADD  CONSTRAINT [__appconfig_active]  DEFAULT ((1)) FOR [active]
GO

ALTER TABLE [dbo].[authAppConfigX] ADD  CONSTRAINT [__appconfig_createdAt]  DEFAULT (getdate()) FOR [createdAt]
GO

ALTER TABLE [dbo].[authAppConfigX] ADD  CONSTRAINT [__appconfig_modifiedAt]  DEFAULT (getdate()) FOR [modifiedAt]
GO

ALTER TABLE [dbo].[authAppConfigX]  WITH CHECK ADD  CONSTRAINT [__fk_appconfig_appid] FOREIGN KEY([appId])
REFERENCES [dbo].[authAppX] ([id])
GO

ALTER TABLE [dbo].[authAppConfigX] CHECK CONSTRAINT [__fk_appconfig_appid]
GO

ALTER TABLE [dbo].[authAppConfigX]  WITH CHECK ADD  CONSTRAINT [__fk_appconfig_configid] FOREIGN KEY([configId])
REFERENCES [dbo].[authAppConfigXRef] ([id])
GO

ALTER TABLE [dbo].[authAppConfigX] CHECK CONSTRAINT [__fk_appconfig_configid]
GO

ALTER TABLE  [dbo].[authAppConfigX] ADD  CONSTRAINT [__uq_appconfig_appconfig]  UNIQUE (appId, configId)
GO

